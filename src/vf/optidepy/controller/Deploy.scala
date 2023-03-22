package vf.optidepy.controller

import utopia.flow.collection.CollectionExtensions._
import utopia.flow.parse.file.FileExtensions._
import utopia.flow.time.TimeExtensions._
import utopia.flow.util.logging.Logger
import vf.optidepy.model.{DeployedProject, Deployment, Project}

import scala.util.{Failure, Success, Try}

/**
 * An interface for deploying projects
 * @author Mikko Hilpinen
 * @since 20.3.2023, v0.1
 */
object Deploy
{
	/**
	 * @param project A project
	 * @param counter A counter for indexing deployments
	 * @param log Logging implementation for non-critical failures
	 * @return Success or failure, containing up-to-date project state.
	 */
	def apply(project: DeployedProject)(implicit counter: IndexCounter, log: Logger): Try[DeployedProject] =
		apply(project.project, project.lastDeployment).map {
			case Some(d) => project + d
			case None => project
		}
	
	/**
	 * Deploys a new project version
	 * @param project Project to deploy
	 * @param lastDeployment Last deployment instance (default = None = not deployed before)
	 * @param counter A counter for indexing deployments
	 * @param log Logging implementation for non-critical failures
	 * @return Success or failure. Success contains the new deployment, if one was made.
	 */
	def apply(project: Project, lastDeployment: Option[Deployment] = None)
	         (implicit counter: IndexCounter, log: Logger) =
	{
		// TODO: Also check for removed files
		project.sourceCorrectedBindings
			.tryFlatMap { binding =>
				val alteredFiles = lastDeployment match {
					// Case: Time threshold specified => Checks which files have been altered
					case Some(d) =>
						binding.source.allChildrenIterator.filter {
							case Success(path) =>
								if (path.isDirectory)
									false
								else
									path.lastModified match {
										case Success(t) => t > d.timestamp
										case Failure(error) =>
											log(error, s"Couldn't read the last modified -time of $path")
											true
									}
							case Failure(_) => true
						}.toTry
					// Case: No time threshold specified => Targets all files
					case None => binding.source.children
				}
				alteredFiles
					.map { _.map { binding / _.relativeTo(binding.source).either }.toVector }
			}
			.flatMap { altered =>
				// Case: No file was altered => No need for a deployment
				if (altered.isEmpty)
					Success(None)
				// Case: Files were altered
				else {
					val deployment = Deployment()
					// Copies the altered files to a specific build directory
					// Updates the full copy -directory, also
					val outputDirectories = Vector(project.directoryForDeployment(deployment), project.fullOutputDirectory)
					altered.tryMap { binding =>
						outputDirectories.tryMap { output =>
							val absolute = binding.underTarget(output)
							absolute.target.createParentDirectories().flatMap { p => absolute.source.copyAs(p) }
						}
					}.map { _ => Some(deployment) }
				}
			}
	}
}
