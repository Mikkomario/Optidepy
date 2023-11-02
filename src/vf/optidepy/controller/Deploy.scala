package vf.optidepy.controller

import utopia.flow.collection.CollectionExtensions._
import utopia.flow.collection.immutable.Pair
import utopia.flow.collection.immutable.caching.LazyTree
import utopia.flow.operator.EqualsFunction
import utopia.flow.parse.file.FileExtensions._
import utopia.flow.time.TimeExtensions._
import utopia.flow.util.TryCatch
import utopia.flow.util.logging.Logger
import utopia.flow.view.immutable.caching.Lazy
import vf.optidepy.model.{Binding, DeployedProject, Deployment, Project}

import java.nio.file.{Path, Paths}
import scala.collection.immutable.VectorBuilder
import scala.io.Codec
import scala.util.{Failure, Success, Try}

/**
 * An interface for deploying projects
 * @author Mikko Hilpinen
 * @since 20.3.2023, v0.1
 */
object Deploy
{
	// ATTRIBUTES   -------------------------
	
	private implicit val iteratorEquals: EqualsFunction[Iterator[Any]] = _ sameElements _
	private implicit val codec: Codec = Codec.UTF8
	
	private val skipComparisonThresholdBytes = 50000
	
	
	// OTHER    -----------------------------
	
	/**
	 * @param project A project
	 * @param skipSeparateBuildDirectory Whether there should not be a separate directory created for this build.
	 * @param counter A counter for indexing deployments
	 * @param log Logging implementation for non-critical failures
	 * @return Success or failure, containing up-to-date project state.
	 */
	def apply(project: DeployedProject, skipSeparateBuildDirectory: Boolean, skipFileRemoval: Boolean)
	         (implicit counter: IndexCounter, log: Logger): Try[DeployedProject] =
		apply(project.project, project.lastDeployment, skipSeparateBuildDirectory, skipFileRemoval).map {
			case Some(d) => project + d
			case None => project
		}
	
	/**
	 * Deploys a new project version
	 * @param project Project to deploy
	 * @param lastDeployment Last deployment instance (default = None = not deployed before)
	 * @param skipSeparateBuildDirectory Whether there should not be a separate directory created for this build.
	 *                                   Default = false = create a separate build directory.
	 * @param skipFileRemoval Whether the file-removal process should be skipped (default = false)
	 * @param counter A counter for indexing deployments
	 * @param log Logging implementation for non-critical failures
	 * @return Success or failure. Success contains the new deployment, if one was made.
	 */
	def apply(project: Project, lastDeployment: Option[Deployment] = None, skipSeparateBuildDirectory: Boolean = false,
	          skipFileRemoval: Boolean = false)
	         (implicit counter: IndexCounter, log: Logger) =
	{
		project.sourceCorrectedBindings
			.tryFlatMap { binding =>
				// Converts the binding to its absolute format (i.e. targeting the right output directory)
				val absoluteBinding = binding.underTarget(project.fullOutputDirectory)
				findAlteredFiles(absoluteBinding, lastDeployment)
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
					val deployment = Deployment()
					// Copies the altered files to a specific build directory (optional feature)
					// Updates the full copy -directory, also
					val buildDirectory = {
						if (skipSeparateBuildDirectory || !project.usesBuildDirectories)
							None
						else
							Some(project.directoryForDeployment(deployment))
					}
					val outputDirectories = buildDirectory.toVector :+ project.fullOutputDirectory
					println(s"Copying ${altered.size} files to ${outputDirectories.mkString(" and ")}...")
					altered.tryMap { binding =>
						outputDirectories.tryMap { output =>
							val absolute = binding.underTarget(output)
							absolute.target.createParentDirectories().flatMap { p => absolute.source.copyAs(p) }
						}
					}.flatMap { _ =>
						println("All files successfully copied")
						// Handles removed files as well (unless disabled)
						if (skipFileRemoval || lastDeployment.isEmpty || !project.fileDeletionEnabled)
							Success(Some(deployment))
						else
							checkForRemovedFiles(project, buildDirectory, deployment.index)
								.logToTry.map { _ => Some(deployment) }
					}
				}
			}
	}
	
	// Finds all files that were modified since the last deployment from the specified binding
	private def findAlteredFiles(absoluteBinding: Binding, lastDeployment: Option[Deployment])(implicit log: Logger) =
	{
		lastDeployment match {
			// Case: Time threshold specified => Checks which files have been altered
			case Some(deployment) =>
				val modifiedFiles = {
					// Case: Source is a regular file => Checks whether it has been modified
					if (absoluteBinding.source.isRegularFile) {
						if (hasChanged(absoluteBinding, absoluteBinding.source, deployment))
							Vector(absoluteBinding.source)
						else
							Vector()
					}
					// Case: Source is a directory => Processes it and collects the files to deploy
					else {
						val deploymentFilesBuilder = new VectorBuilder[Path]()
						// TODO: Use a separate logger in .toTree that fails the process if an error is encountered
						findAlteredFilesFromDirectory(absoluteBinding, deployment, absoluteBinding.source.toTree,
							deploymentFilesBuilder)
						deploymentFilesBuilder.result()
					}
				}
				Success(modifiedFiles)
			// Case: No time threshold specified => Targets all files
			case None => absoluteBinding.source.children
		}
	}
	
	private def findAlteredFilesFromDirectory(binding: Binding, lastDeployment: Deployment,
	                                          directoryNode: LazyTree[Path],
	                                          deploymentFilesBuilder: VectorBuilder[Path])
	                                         (implicit log: Logger): Unit =
	{
		// Check whether the directory exists in the target destination
		val relativePath = directoryNode.nav.relativeTo(binding.source).either
		val targetDirectory = binding.target/relativePath
		// Case: Target directory exists => Compare individual files and/or move down in the tree
		if (targetDirectory.exists) {
			val (subDirectories, files) = directoryNode.children.divideBy { _.nav.isRegularFile }.toTuple
			
			// Adds modified files from this directory to the deployment
			deploymentFilesBuilder ++= files.iterator.map { _.nav }.filter { hasChanged(binding, _, lastDeployment) }
			
			// Handles the sub-directories using recursion
			subDirectories.foreach { subDirectory =>
				findAlteredFilesFromDirectory(binding, lastDeployment, subDirectory, deploymentFilesBuilder)
			}
		}
		// Case: Target directory doesn't exist => Adds all files under this directory to be deployed
		else
			deploymentFilesBuilder ++= directoryNode.nodesBelowIterator.map { _.nav }.filter { _.isRegularFile }
	}
	
	private def checkForRemovedFiles(project: Project, buildDirectory: => Option[Path], deploymentIndex: => Int) =
	{
		println(s"Checking for removed files from ${project.fullOutputDirectory}...")
		val backupDir = buildDirectory.map { dir => Lazy { dir/"deleted-files" } }
		// Groups the bindings into different categories:
		//      1) File-specific bindings
		//      2) Bindings that map directly to the full output directory
		//      3) Other bindings
		val bindings = project.sourceCorrectedBindings.map { _.underTarget(project.fullOutputDirectory) }
		val (directoryBindings, fileSpecificBindings) = bindings.divideBy { _.target.isRegularFile }.toTuple
		val (otherBindings, mappedBindings) = directoryBindings.map { b => b -> b.target }
			.divideBy { _._2 ~== project.fullOutputDirectory }
			// Sets the relative path of immediately mapping bindings
			.mapSecond { _.map { _._1 -> Paths.get("") } }
			.toTuple
		
		val deletedFilesBuilder = new VectorBuilder[Path]()
		val failuresBuilder = new VectorBuilder[Throwable]()
		// Performs the file-deletion process
		val deleteResult = handleFileDeletion(project.fullOutputDirectory, Lazy { Paths.get("") },
			mappedBindings, otherBindings, fileSpecificBindings.map { _.target }.toSet, backupDir,
			deletedFilesBuilder, failuresBuilder)
		val deletedFiles = deletedFilesBuilder.result()
		
		// Writes a note concerning the deleted files, if any were found
		if (deletedFiles.nonEmpty) {
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
	                                mappedBindings: Iterable[(Binding, Path)],
	                                otherBindings: Iterable[(Binding, Path)], keepFiles: Set[Path],
	                                backupDir: Option[Lazy[Path]], deletedPathsBuilder: VectorBuilder[Path],
	                                failuresBuilder: VectorBuilder[Throwable]): Try[Unit] =
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
						println(s"Matching source directories: ${sourceDirectories.mkString(", ")}")
						println(s"Files to keep: ${keepFiles.mkString(", ")}")
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
				subDirectories.tryForeach { subDirectory =>
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
					
					handleFileDeletion(targetDirectory/dirName, relativeTargetDirectory.map { _/dirName },
						nextMappedBindings, remainingOtherBindings,
						keepFiles, backupDir.map { _.map { _/dirName } }, deletedPathsBuilder, failuresBuilder)
				}
			}
	}
	
	private def hasChanged(binding: Binding, file: Path, lastDeployment: Deployment)(implicit log: Logger) = {
		val lastModifiedChanged = file.lastModified match {
			// Case: Last modified available => Checks whether it has updated
			case Success(t) => t > lastDeployment.timestamp
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
