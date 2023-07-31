package vf.optidepy.controller

import utopia.flow.collection.CollectionExtensions._
import utopia.flow.collection.immutable.Pair
import utopia.flow.operator.EqualsFunction
import utopia.flow.parse.file.FileExtensions._
import utopia.flow.time.TimeExtensions._
import utopia.flow.util.logging.Logger
import vf.optidepy.model.{DeployedProject, Deployment, Project}

import java.nio.file.Path
import scala.io.Codec
import scala.util.{Failure, Success, Try}

/**
 * An interface for deploying projects
 * @author Mikko Hilpinen
 * @since 20.3.2023, v0.1
 */
object Deploy
{
	private implicit val iteratorEquals: EqualsFunction[Iterator[Any]] = _ sameElements _
	private implicit val codec: Codec = Codec.UTF8
	
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
				lazy val absoluteBinding = binding.underTarget(project.fullOutputDirectory)
				val alteredFiles = lastDeployment match {
					// Case: Time threshold specified => Checks which files have been altered
					case Some(d) =>
						println(s"Looking for new and modified files under ${binding.source}...")
						binding.source.allChildrenIterator
							.filter {
								case Success(path) =>
									// Only moves regular files
									if (path.isDirectory)
										false
									else
										path.lastModified match {
											// Case: Last modified available => Checks whether it has updated
											case Success(t) => t > d.timestamp
											// Case: Last-modified couldn't be read => Considers it changed
											case Failure(error) =>
												log(error, s"Couldn't read the last modified -time of $path")
												true
										}
								case Failure(_) => true
							}
							// Also makes sure either the file size or file contents are different
							.filter {
								case Success(path) =>
									val relativePath = path.relativeTo(binding.source).either
									val fullTargetPath = absoluteBinding.target/relativePath
									hasUpdated(path, fullTargetPath)
								case Failure(_) => true
							}
							.toTry
					// Case: No time threshold specified => Targets all files
					case None => binding.source.children
				}
				alteredFiles
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
							checkForRemovedFiles(project, buildDirectory, deployment.index).map { _ => Some(deployment) }
					}
				}
			}
	}
	
	private def checkForRemovedFiles(project: Project, buildDirectory: => Option[Path], deploymentIndex: => Int)
	                                (implicit log: Logger) =
	{
		lazy val backupDir = buildDirectory.map { _/"deleted-files" }
		// Deletes all files from the "full" directory that no longer appear under the project source
		project.sourceCorrectedBindings.map { _.underTarget(project.fullOutputDirectory) }.groupBy { _.target }
			.flatMap { case (outputDir, bindings) =>
				println(s"Deleting old files from $outputDir ...")
				outputDir.toTree.bottomToTopNodesIterator.flatMap { node =>
					val targetPath = node.nav
					val relativePath = targetPath.relativeTo(outputDir).either
					val sourcePaths = bindings.map { _.source/relativePath }.toSet
					
					// Case: File was deleted from the source => Deletes/moves the file from "full" and records the event
					if (sourcePaths.forall { _.notExists }) {
						// If a backup directory is specified, moves the file instead of deleting it
						Some(backupDir match {
							case Some(dir) =>
								(dir / relativePath).createDirectories()
									.flatMap { targetPath.moveAs(_).map { _ => relativePath } }
							case None => targetPath.delete().map { _ => relativePath }
						})
					}
					// Case: File still present => No action needed
					else
						None
				}
			}
			.toTryCatch.logToTryWithMessage("Failed to delete some of the old files")
			.flatMap { deleted =>
				// Records the deleted files to the build directory (build mode only)
				if (deleted.nonEmpty)
					buildDirectory match {
						// Case: Files were deleted and build directory was specified => Writes a note
						case Some(buildDirectory) =>
							(buildDirectory/s"deleted-files-$deploymentIndex.txt").writeUsing { writer =>
								writer.println(s"The following ${deleted.size} files were deleted in this build:")
								deleted.map { _.toString }.sorted.foreach { p => writer.println(s"\t- $p") }
							}.map { Some(_) }
						case None => Success(None)
					}
				else
					Success(None)
			}
	}
	
	// Tests whether two paths should be considered different files.
	// Intended for regular files only.
	// Last modified should be tested first.
	private def hasUpdated(source: Path, target: Path) = {
		// Case: No target file present => Considers changed
		if (!target.exists)
			true
		// Case: Files have different sizes => Changed
		else if (source.size.toOption.forall { size => !target.size.toOption.contains(size) })
			true
		// Case: Same size => Checks whether the content is equal, also
		else
			source.tryReadWith { sourceStream =>
				target.readWith { targetStream =>
					// Compares the first < 100 000 bytes of both files
					Pair(sourceStream, targetStream)
						.map { stream => Iterator.continually { stream.read() }.take(100000).takeWhile { _ >= 0 } }
						.isAsymmetric
				}
			}.getOrElse(true) // Considers changed on a read failure
	}
}
