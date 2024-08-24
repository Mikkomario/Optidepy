package vf.optidepy.model.combined.deployment

import vf.optidepy.model.stored.deployment.{Binding, DeploymentConfig}

object DeploymentConfigWithBindings
{
	// OTHER	--------------------
	
	/**
	  * @param config config to wrap
	  * @param binding binding to attach to this config
	  * @return Combination of the specified config and binding
	  */
	def apply(config: DeploymentConfig, binding: Seq[Binding]): DeploymentConfigWithBindings =
		_DeploymentConfigWithBindings(config, binding)
	
	
	// NESTED	--------------------
	
	/**
	  * @param config config to wrap
	  * @param bindings binding to attach to this config
	  */
	private case class _DeploymentConfigWithBindings(config: DeploymentConfig, bindings: Seq[Binding])
		extends DeploymentConfigWithBindings
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
trait DeploymentConfigWithBindings extends CombinedDeploymentConfig[DeploymentConfigWithBindings]
{
	// ABSTRACT	--------------------
	
	/**
	 * @return The wrapped deployment config
	 */
	def config: DeploymentConfig
	/**
	  * Binding that are attached to this config
	  */
	def bindings: Seq[Binding]
	
	
	// IMPLEMENTED	--------------------
	
	override def deploymentConfig = config
}

