package vf.optidepy.app.action

import utopia.flow.parse.file.FileExtensions._
import utopia.flow.util.StringExtensions._
import utopia.flow.util.logging.Logger
import utopia.vault.database.Connection
import vf.optidepy.controller.IndexCounter
import vf.optidepy.controller.deployment.Deploy2
import vf.optidepy.database.access.many.deployment.DbDeployments
import vf.optidepy.database.storable.deployment.DeploymentDbModel
import vf.optidepy.model.combined.deployment.{PossiblyDeployedBranch, ProjectDeploymentConfigWithBindings}

import java.time.Instant
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

/**
 * An interface that provides interactive deployment-related functions
 *
 * @author Mikko Hilpinen
 * @since 23.08.2024, v1.2
 */
object DeploymentActions
{
	// TODO: Modify this function to accept config / project and branch names as strings and request user for the missing information
	def deploy(config: ProjectDeploymentConfigWithBindings, branch: PossiblyDeployedBranch,
	           includeChangesAfter: Option[Instant] = None, fullRebuild: Boolean = false)
	          (implicit connection: Connection, exc: ExecutionContext, log: Logger): Unit =
	{
		// Determines the next deployment index
		implicit val counter: IndexCounter = new IndexCounter(DbDeployments.ofBranch(branch.id).latest
			.deploymentIndex match
		{
			case Some(lastIndex) => lastIndex + 1
			case None => 1
		})
		
		// Performs the deployment
		println(s"Deploying ${ config.name.nonEmptyOrElse(config.project.name) }${
			branch.name.mapIfNotEmpty { b => s" ($b)" } }")
		Deploy2(config, branch, includeChangesAfter, fullRebuild = fullRebuild) match {
			case Success(deployment) =>
				deployment match {
					case Some(deployment) =>
						println("Deployment complete!")
						
						// Stores the deployment into the DB
						DeploymentDbModel.insert(deployment)
						
						// May open the deployment directory afterwards
						if (config.usesBuildDirectories)
							config.directoryForDeployment(branch.name, deployment).openDirectory()
					
					case None => println("No files were deployed")
				}
			
			case Failure(error) => log(error, "Deployment failed")
		}
	}
}
