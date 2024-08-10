package vf.optidepy.model.combined.deployment

import utopia.flow.view.template.Extender
import utopia.vault.model.template.HasId
import vf.optidepy.model.factory.deployment.DeploymentConfigFactoryWrapper
import vf.optidepy.model.partial.deployment.DeploymentConfigData
import vf.optidepy.model.stored.deployment.DeploymentConfig

/**
  * Common trait for combinations that add additional data to deployment configs
  * @tparam Repr Type of the implementing class
  * @author Mikko Hilpinen
  * @since 10.08.2024, v1.2
  */
trait CombinedDeploymentConfig[+Repr] 
	extends Extender[DeploymentConfigData] with HasId[Int] 
		with DeploymentConfigFactoryWrapper[DeploymentConfig, Repr]
{
	// ABSTRACT	--------------------
	
	/**
	  * Wrapped deployment config
	  */
	def deploymentConfig: DeploymentConfig
	
	
	// IMPLEMENTED	--------------------
	
	/**
	  * Id of this deployment config in the database
	  */
	override def id = deploymentConfig.id
	
	override def wrapped = deploymentConfig.data
	
	override protected def wrappedFactory = deploymentConfig
}

