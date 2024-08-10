package vf.optidepy.model.combined.deployment

import vf.optidepy.model.stored.deployment.{Binding, DeploymentConfig}

object DetailedDeploymentConfig
{
	// OTHER	--------------------
	
	/**
	  * @param config config to wrap
	  * @param binding binding to attach to this config
	  * @return Combination of the specified config and binding
	  */
	def apply(config: DeploymentConfig, binding: Seq[Binding]): DetailedDeploymentConfig = 
		_DetailedDeploymentConfig(config, binding)
	
	
	// NESTED	--------------------
	
	/**
	  * @param config config to wrap
	  * @param binding binding to attach to this config
	  */
	private case class _DetailedDeploymentConfig(config: DeploymentConfig, binding: Seq[Binding]) 
		extends DetailedDeploymentConfig
	{
		// IMPLEMENTED	--------------------
		
		override protected def wrap(factory: DeploymentConfig) = copy(config = factory)
	}
}

/**
  * Includes individual deployment bindings with the general configuration
  * @author Mikko Hilpinen
  * @since 10.08.2024, v1.2
  */
trait DetailedDeploymentConfig extends CombinedDeploymentConfig[DetailedDeploymentConfig]
{
	// ABSTRACT	--------------------
	
	/**
	 * @return The wrapped deployment config
	 */
	def config: DeploymentConfig
	/**
	  * Binding that are attached to this config
	  */
	def binding: Seq[Binding]
	
	
	// IMPLEMENTED	--------------------
	
	override def deploymentConfig = config
}

