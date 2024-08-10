package vf.optidepy.model.combined.deployment

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
	
	
	// IMPLEMENTED	--------------------
	
	override def deploymentConfig: DeploymentConfig = config
}

