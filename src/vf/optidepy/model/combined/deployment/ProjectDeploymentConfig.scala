package vf.optidepy.model.combined.deployment

import utopia.flow.view.template.Extender
import utopia.vault.model.template.HasId
import vf.optidepy.model.factory.deployment.DeploymentConfigFactoryWrapper
import vf.optidepy.model.partial.deployment.DeploymentConfigData
import vf.optidepy.model.stored.deployment.DeploymentConfig
import vf.optidepy.model.stored.project.Project

/**
  * Combines project and deployment configuration information
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class ProjectDeploymentConfig(config: DeploymentConfig, project: Project) 
	extends Extender[DeploymentConfigData] with HasId[Int] 
		with DeploymentConfigFactoryWrapper[DeploymentConfig, ProjectDeploymentConfig]
{
	// COMPUTED	--------------------
	
	/**
	  * Id of this config in the database
	  */
	def id = config.id
	
	
	// IMPLEMENTED	--------------------
	
	override def wrapped = config.data
	
	override protected def wrappedFactory = config
	
	override protected def wrap(factory: DeploymentConfig) = copy(config = factory)
}

