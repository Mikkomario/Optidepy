package vf.optidepy.controller.deployment

import utopia.flow.async.AsyncExtensions._
import utopia.flow.async.context.ActionQueue
import utopia.flow.collection.CollectionExtensions._
import utopia.flow.collection.immutable.Pair
import utopia.flow.collection.immutable.caching.LazyTree
import utopia.flow.event.listener.ProgressListener
import utopia.flow.operator.equality.EqualsFunction
import utopia.flow.parse.file.FileExtensions._
import utopia.flow.time.TimeExtensions._
import utopia.flow.util.logging.Logger
import utopia.flow.util.{ProgressTracker, TryCatch}
import utopia.flow.util.TryExtensions._
import utopia.flow.util.EitherExtensions._
import utopia.flow.view.immutable.caching.Lazy
import utopia.flow.view.mutable.async.Volatile
import vf.optidepy.controller.IndexCounter
import vf.optidepy.model.combined.deployment.{ProjectDeploymentConfigWithBindings, PossiblyDeployedBranch}
import vf.optidepy.model.partial.deployment.DeploymentData
import vf.optidepy.model.template.deployment.HasBindingProps

import java.nio.file.{Path, Paths}
import java.time.Instant
import scala.collection.immutable.VectorBuilder
import scala.concurrent.{ExecutionContext, Future}
import scala.io.Codec
import scala.util.{Failure, Success, Try}

/**
 * An interface for deploying projects
 * @author Mikko Hilpinen
 * @since 20.3.2023, v0.1
 */
object Deploy2
{
	// ATTRIBUTES   -------------------------
	
	private implicit val iteratorEquals: EqualsFunction[Iterator[Any]] = _ sameElements _
	private implicit val codec: Codec = Codec.UTF8
	
	private val skipComparisonThresholdBytes = 50000
	private val maxParallelThreads = 20
	
	
	// OTHER    -----------------------------
	
	/**
	 * Deploys a new project version
	 * @param config Deployment configurations
	 * @param branch The branch to deploy, including information about the latest deployment, if applicable
	 * @param includeChangesAfter A time threshold after which all changes are included
	 *                            regardless of the last deployment time.
	 *                            Default = None = include changes after the last deployment.
	 * @param skipSeparateBuildDirectory Whether there should not be a separate directory created for this build.
	 *                                   Default = false = create a separate build directory.
	 * @param skipFileRemoval Whether the file-removal process should be skipped (default = false)
	 * @param fullRebuild Whether to perform a full rebuild and not just copy the changed files. Default = false.
	 * @param counter A counter for indexing deployments
	 * @param log Logging implementation for non-critical failures
	 * @return Success or failure. Success contains the new deployment, if one was made.
	 */
	def apply(config: ProjectDeploymentConfigWithBindings, branch: PossiblyDeployedBranch,
	          includeChangesAfter: Option[Instant] = None,
	          skipSeparateBuildDirectory: Boolean = false, skipFileRemoval: Boolean = false,
	          fullRebuild: Boolean = false)
	         (implicit counter: IndexCounter, log: Logger, exc: ExecutionContext) =
	{
		val fullOutputDirectory = config.outputDirectoryFor(branch.name)
		lazy val lastDeploymentTime = {
			if (fullRebuild)
				None
			else
				branch.deployment.map { _.created }.map { lastBranchDeployment =>
					includeChangesAfter match {
						case Some(inclusionThreshold) => lastBranchDeployment min inclusionThreshold
						case None => lastBranchDeployment
					}
				}
		}
		
		config.sourceCorrectedBindings
			.tryFlatMap { binding =>
				// Converts the binding to its absolute format (i.e. targeting the right output directory)
				val absoluteBinding = binding.underTarget(fullOutputDirectory)
				findAlteredFiles(absoluteBinding, lastDeploymentTime)
					// Transforms the modified files into file-specific binding instances
					.map { _.map { binding / _.relativeTo(binding.source).either }.toVector }
			}
			.flatMap { altered =>
				// Case: No file was altered => No need for a deployment
				if (altered.isEmpty) {
					println("Everything is up to date!")
					Success(None)
				}
				// Case: Files were altered
				else {
					val deployment = DeploymentData(branch.id)
					// Copies the altered files to a specific build directory (optional feature)
					// Updates the full copy -directory, also
					val buildDirectory = {
						if (skipSeparateBuildDirectory || !config.usesBuildDirectories)
							None
						else
							Some(config.directoryForDeployment(branch.name, deployment))
					}
					val outputDirectories = buildDirectory.toVector :+ fullOutputDirectory
					val targetFileCount = altered.size
					println(s"Copying $targetFileCount files to ${outputDirectories.mkString(" and ")}...")
					val actionQueue = new ActionQueue(maxParallelThreads)
					val progressTracker = ProgressTracker.wrap(Volatile.eventful(0)) { _.toDouble / targetFileCount }
					progressTracker.addListener(ProgressListener.usingThreshold[Int](0.1) { e =>
						println(s"${ e.value }/$targetFileCount files copied (${ (e.currentProgress * 100).toInt }%) - ${
							e.projectedRemainingDuration.description } remaining")
					})
					altered
						.tryMap { binding =>
							outputDirectories.tryMap { output =>
								val absolute = binding.underTarget(output)
								absolute.target.createParentDirectories().map { p =>
									// Uses multi-threading in file copy
									val action = actionQueue.push {
										val res = absolute.source.copyAs(p)
										progressTracker.value += 1
										res
									}
									action.waitUntilStarted()
									action
								}
							}
						}
						// TODO: Add better logging system here
						.flatMap { actions => actions.flatten.map { _.future.waitForResult() }.toTry }
						.flatMap { _ =>
							println("All files successfully copied")
							// Handles removed files as well (unless disabled)
							if (skipFileRemoval || lastDeploymentTime.isEmpty || !config.fileDeletionEnabled)
								Success(Some(deployment))
							else
								checkForRemovedFiles(config, branch.name, buildDirectory, deployment.deploymentIndex)
									.logToTry.map { _ => Some(deployment) }
						}
				}
			}
	}
	
	// Finds all files that were modified since the last deployment from the specified binding
	private def findAlteredFiles(absoluteBinding: HasBindingProps, lastDeploymentTime: Option[Instant])
	                            (implicit log: Logger, exc: ExecutionContext) =
	{
		lastDeploymentTime match {
			// Case: Time threshold specified => Checks which files have been altered
			case Some(deploymentTime) =>
				println(s"Looking for files that have been modified since ${
					deploymentTime.toLocalDateTime} from ${absoluteBinding.source}...")
				val modifiedFiles = {
					// Case: Source is a regular file => Checks whether it has been modified
					if (absoluteBinding.source.isRegularFile) {
						if (hasChanged(absoluteBinding, absoluteBinding.source, deploymentTime))
							Vector(absoluteBinding.source)
						else
							Vector()
					}
					// Case: Source is a directory => Processes it and collects the files to deploy
					else {
						val deploymentFilesBuilder = new VectorBuilder[Path]()
						val actionQueue = new ActionQueue(maxParallelThreads)
						// TODO: Use a separate logger in .toTree that fails the process if an error is encountered
						findAlteredFilesFromDirectory(absoluteBinding, deploymentTime, absoluteBinding.source.toTree,
							deploymentFilesBuilder, actionQueue)
							.waitFor()
						deploymentFilesBuilder.result()
					}
				}
				Success(modifiedFiles)
			// Case: No time threshold specified => Targets all files
			case None => absoluteBinding.source.children
		}
	}
	
	private def findAlteredFilesFromDirectory(binding: HasBindingProps, lastDeploymentTime: Instant,
	                                          directoryNode: LazyTree[Path],
	                                          deploymentFilesBuilder: VectorBuilder[Path], actionQueue: ActionQueue)
	                                          (implicit log: Logger, exc: ExecutionContext): Future[Unit] =
	{
		// Check whether the directory exists in the target destination
		val relativePath = directoryNode.nav.relativeTo(binding.source).either
		val targetDirectory = binding.target/relativePath
		// Case: Target directory exists => Compare individual files and/or move down in the tree
		if (targetDirectory.exists) {
			val (subDirectories, files) = directoryNode.children.divideBy { _.nav.isRegularFile }.toTuple
			
			// Adds modified files from this directory to the deployment
			deploymentFilesBuilder ++= files.iterator.map { _.nav }
				.filter { hasChanged(binding, _, lastDeploymentTime) }
			
			// Handles the sub-directories using recursion and multi-threading
			if (subDirectories.nonEmpty)
				Future {
					subDirectories.map { subDirectory =>
						val action = actionQueue.push {
							findAlteredFilesFromDirectory(binding, lastDeploymentTime, subDirectory,
								deploymentFilesBuilder, actionQueue).waitFor()
						}
						action.waitUntilStarted()
						action
					}.map { _.future.waitForResult() }.failuresIterator
						.foreach { log(_, s"Failed to find altered files from under ${ directoryNode.nav }") }
				}
			else
				Future.unit
		}
		// Case: Target directory doesn't exist => Adds all files under this directory to be deployed
		else {
			deploymentFilesBuilder += directoryNode.nav
			Future.unit
		}
	}
	
	private def checkForRemovedFiles(project: ProjectDeploymentConfigWithBindings, branchName: String,
	                                 buildDirectory: => Option[Path], deploymentIndex: => Int)
	                                (implicit exc: ExecutionContext) =
	{
		val fullOutputDirectory = project.outputDirectoryFor(branchName)
		println(s"Checking for removed files from $fullOutputDirectory...")
		val backupDir = buildDirectory.map { dir => Lazy { dir/"deleted-files" } }
		// Groups the bindings into different categories:
		//      1) File-specific bindings
		//      2) Bindings that map directly to the full output directory
		//      3) Other bindings
		val bindings = project.sourceCorrectedBindings.map { _.underTarget(fullOutputDirectory) }
		val (directoryBindings, fileSpecificBindings) = bindings.divideBy { _.target.isRegularFile }.toTuple
		val (otherBindings, mappedBindings) = directoryBindings.map { b => b -> b.target }
			.divideBy { _._2 ~== fullOutputDirectory }
			// Sets the relative path of immediately mapping bindings
			.mapSecond { _.map { _._1 -> Paths.get("") } }
			.toTuple
		
		val deletedFilesBuilder = new VectorBuilder[Path]()
		val failuresBuilder = new VectorBuilder[Throwable]()
		// Performs the file-deletion process
		val deleteResult = handleFileDeletion(fullOutputDirectory, Lazy { Paths.get("") },
			mappedBindings, otherBindings, fileSpecificBindings.map { _.target }.toSet, backupDir,
			deletedFilesBuilder, failuresBuilder)
		val deletedFiles = deletedFilesBuilder.result()
		
		// Writes a note concerning the deleted files, if any were found
		if (deletedFiles.nonEmpty) {
			// TODO: Document these deleted files in the database
			println(s"Deleted ${deletedFiles.size} files")
			val noteWriteResult = buildDirectory match {
				// Case: Files were deleted and build directory was specified => Writes a note
				case Some(buildDirectory) =>
					(buildDirectory / s"deleted-files-$deploymentIndex.txt").writeUsing { writer =>
						writer.println(s"The following ${deletedFiles.size} files were deleted in this build:")
						deletedFiles.map { _.toString }.sorted.foreach { p => writer.println(s"\t- $p") }
					}
				case None => Success(())
			}
			TryCatch.Success((), failuresBuilder.result() ++ deleteResult.failure ++ noteWriteResult.failure)
		}
		else {
			println("No files were deleted")
			deleteResult.toTryCatch.withAdditionalFailures(failuresBuilder.result())
		}
	}
	
	// RECURSIVE
	// - Assumes all bindings are set to correct source and full deployment environments
	// - Mapped bindings are coupled with the relative path that matches this directory
	// - Other bindings are coupled with their target deployment directories
	//   that may appear somewhere under this directory
	// - Keep files are individual deployment files that should be preserved and not deleted
	//   (based on file-specific bindings)
	// - Backup directory matches this directory
	private def handleFileDeletion(targetDirectory: Path, relativeTargetDirectory: Lazy[Path],
	                               mappedBindings: Iterable[(HasBindingProps, Path)],
	                               otherBindings: Iterable[(HasBindingProps, Path)], keepFiles: Set[Path],
	                               backupDir: Option[Lazy[Path]], deletedPathsBuilder: VectorBuilder[Path],
	                               failuresBuilder: VectorBuilder[Throwable])
	                              (implicit exc: ExecutionContext): Try[Unit] =
	{
		// Divides the directly accessible files into directories and regular files
		targetDirectory.iterateChildren { _.divideBy { _.isRegularFile }.map { _.toVector } }
			.flatMap { case Pair(subDirectories, files) =>
				// Checks whether the regular files appear under some of the mapped bindings
				if (files.nonEmpty) {
					lazy val sourceDirectories = mappedBindings
						.map { case (binding, relative) => binding.source/relative }
					val filesToDelete = files.iterator
						.filter { file =>
							// Won't include entries listed under "keepFiles"
							!keepFiles.contains(file) &&
								sourceDirectories.forall { dir => (dir/file.fileName).notExists }
						}
						.toVector
					if (filesToDelete.nonEmpty) {
						println(s"Deletes ${filesToDelete.size} files under $targetDirectory")
						// Records the deleted files
						deletedPathsBuilder ++= filesToDelete.map { f => relativeTargetDirectory.value/f.fileName }
						// Performs the deletion or move-to-backup operation
						val failures = backupDir match {
							// Case: Backup files instead of deleting them
							case Some(lazyBackUpDir) =>
								filesToDelete.flatMap { _.moveTo(lazyBackUpDir.value).failure }
							// Case: Delete files
							case None => filesToDelete.flatMap { _.delete().failure }
						}
						// Records encountered failures
						failuresBuilder ++= failures
					}
				}
				// Recursively moves down to the sub-directories
				// Uses multi-threading in order to improve processing speed
				// FIXME: Should handle failures using a logger and not terminate the process. Now continues the process randomly for a while anyway.
				if (subDirectories.nonEmpty)
					subDirectories.tryMap { subDirectory =>
						val dirName = subDirectory.fileName
						// Collects those "other" bindings that match this sub-directory and adds them to mapped bindings
						val (remainingOtherBindings, newMappedBindings) = otherBindings.divideBy { _._2 ~== subDirectory }
							// Filters out the "other" bindings that can't appear under this sub-directory
							.mapFirst { _.filter { _._2.isChildOf(subDirectory) } }
							// Assigns correct relative path to the new mappings
							.mapSecond { _.map { case (b, _) => b -> Paths.get("") } }
							.toTuple
						// Updates the relative paths of existing mapped bindings
						val nextMappedBindings = mappedBindings.map { case (b, relative) => b -> (relative/dirName) } ++
							newMappedBindings
						
						// Deletes the targeted files within this directory
						handleFileDeletion(targetDirectory/dirName, relativeTargetDirectory.map { _/dirName },
							nextMappedBindings, remainingOtherBindings,
							keepFiles, backupDir.map { _.map { _/dirName } }, deletedPathsBuilder, failuresBuilder)
							// If successful, checks whether this directory is now empty
							// If so, deletes it
							.flatMap { _ =>
								if (subDirectory.isEmpty) {
									println(s"Deleting the empty sub-directory: $subDirectory")
									subDirectory.delete()
								}
								else
									Success(false)
							}
					}.map { _ => () }
				else
					Success(())
			}
	}
	
	private def hasChanged(binding: HasBindingProps, file: Path, lastDeploymentTime: Instant)(implicit log: Logger) = {
		val lastModifiedChanged = file.lastModified match {
			// Case: Last modified available => Checks whether it has updated
			case Success(t) => t > lastDeploymentTime
			// Case: Last-modified couldn't be read => Considers it changed
			case Failure(error) =>
				log(error, s"Couldn't read the last modified -time of $file")
				true
		}
		// Case: The last-modified-time was changed =>
		// Also makes sure either the file size or file contents are different
		if (lastModifiedChanged) {
			val relativePath = file.relativeTo(binding.source).either
			val fullTargetPath = binding.target / relativePath
			fileContentHasUpdated(file, fullTargetPath)
		}
		else
			false
	}
	
	// Tests whether two paths should be considered different files.
	// Intended for regular files only.
	// Last modified should be tested first.
	private def fileContentHasUpdated(source: Path, target: Path) = {
		// Case: No target file present => Considers changed
		if (!target.exists)
			true
		else {
			val sourceSize = source.size.toOption
			// Case: Files have different sizes (or too large files for byte-comparison) => Changed
			if (sourceSize.forall { size => size >= skipComparisonThresholdBytes || !target.size.toOption.contains(size) })
				true
			// Case: Same size => Checks whether the content is equal, also (skipped for very large files)
			else
				source.tryReadWith { sourceStream =>
					target.readWith { targetStream =>
						// Compares the first < 100 000 bytes of both files
						Pair(sourceStream, targetStream)
							.map { stream => Iterator.continually { stream.read() }.takeWhile { _ >= 0 } }
							.isAsymmetric
					}
				}.getOrElse(true) // Considers changed on a read failure
		}
	}
}
