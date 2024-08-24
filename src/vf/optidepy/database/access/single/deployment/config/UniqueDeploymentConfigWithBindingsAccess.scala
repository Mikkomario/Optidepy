package vf.optidepy.database.access.single.deployment.config

import utopia.vault.nosql.view.{NullDeprecatableView, ViewFactory}
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.deployment.DeploymentConfigWithBindingsDbFactory
import vf.optidepy.database.storable.deployment.BindingDbModel
import vf.optidepy.model.combined.deployment.DeploymentConfigWithBindings

object UniqueDeploymentConfigWithBindingsAccess extends ViewFactory[UniqueDeploymentConfigWithBindingsAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): UniqueDeploymentConfigWithBindingsAccess =
		_UniqueDeploymentConfigWithBindingsAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _UniqueDeploymentConfigWithBindingsAccess(override val accessCondition: Option[Condition])
		extends UniqueDeploymentConfigWithBindingsAccess
}

/**
  * A common trait for access points that return distinct detailed deployment configs
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait UniqueDeploymentConfigWithBindingsAccess
	extends UniqueDeploymentConfigAccessLike[DeploymentConfigWithBindings, UniqueDeploymentConfigWithBindingsAccess]
		with NullDeprecatableView[UniqueDeploymentConfigWithBindingsAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * A database model (factory) used for interacting with the linked binding
	  */
	protected def bindingModel = BindingDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = DeploymentConfigWithBindingsDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): UniqueDeploymentConfigWithBindingsAccess =
		UniqueDeploymentConfigWithBindingsAccess(condition)
}

