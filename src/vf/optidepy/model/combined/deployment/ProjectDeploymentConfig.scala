package vf.optidepy.model.combined.deployment

import utopia.flow.parse.file.FileExtensions._
import utopia.flow.time.TimeExtensions._
import vf.optidepy.model.partial.deployment.DeploymentData
import vf.optidepy.model.stored.deployment.DeploymentConfig
import vf.optidepy.model.stored.project.Project

object ProjectDeploymentConfig
{
	// OTHER    ------------------------------
	
	/**
	 * @param config Config to wrap
	 * @param project Deployed project
	 * @return Specified configuration with the specified project included
	 */
	def apply(config: DeploymentConfig, project: Project): ProjectDeploymentConfig =
		_ProjectDeploymentConfig(config, project)
	
	
	// NESTED   ------------------------------
	
	private case class _ProjectDeploymentConfig(config: DeploymentConfig, project: Project)
		extends ProjectDeploymentConfig
	{
		override protected def wrap(factory: DeploymentConfig) = copy(config = factory)
	}
}

/**
  * Combines project and deployment configuration information
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait ProjectDeploymentConfig extends CombinedDeploymentConfig[ProjectDeploymentConfig]
{
	// ABSTRACT --------------------
	
	/**
	 * @return Wrapped deployment configuration
	 */
	def config: DeploymentConfig
	/**
	 * @return Deployed project
	 */
	def project: Project
	
	
	// COMPUTED	--------------------
	
	/**
	 * @return Directory under which all deployed files will be placed
	 */
	def outputDirectory = project.rootPath/config.outputDirectory
	
	/**
	 * @param branch Name of the targeted branch
	 * @return The directory that will contain the full project output for that branch
	 */
	def fullOutputDirectoryFor(branch: String) = outputDirectory / branch
	
	
	// IMPLEMENTED	--------------------
	
	override def deploymentConfig: DeploymentConfig = config
	
	
	// OTHER    ------------------------
	
	/**
	 * @param branch Name of the deployed branch
	 * @param deployment Targeted deployment
	 * @return A directory where that deployment should be stored
	 */
	def directoryForDeployment(branch: String, deployment: DeploymentData) = {
		val versionStr = deployment.version match {
			case Some(version) => s"-${version.toString.replace('.', '-')}"
			case None => ""
		}
		outputDirectory/s"$branch$versionStr-build-${ deployment.index }-${deployment.created.toLocalDate.toString}"
	}
}

