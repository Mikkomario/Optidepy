package vf.optidepy.model.combined.deployment

import utopia.flow.view.template.Extender
import utopia.vault.model.template.HasId
import vf.optidepy.model.factory.deployment.DeploymentConfigFactoryWrapper
import vf.optidepy.model.partial.deployment.DeploymentConfigData
import vf.optidepy.model.stored.deployment.{Binding, DeploymentConfig}

/**
  * Includes individual deployment bindings with the general configuration
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DetailedDeploymentConfig(config: DeploymentConfig, binding: Seq[Binding]) 
	extends Extender[DeploymentConfigData] with HasId[Int]
		with DeploymentConfigFactoryWrapper[DeploymentConfig, DetailedDeploymentConfig]
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

