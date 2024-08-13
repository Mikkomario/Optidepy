package vf.optidepy.database.access.single.deployment.config

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.view.{NullDeprecatableView, ViewFactory}
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.deployment.DeploymentConfigDbFactory
import vf.optidepy.model.stored.deployment.DeploymentConfig

object UniqueDeploymentConfigAccess extends ViewFactory[UniqueDeploymentConfigAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): UniqueDeploymentConfigAccess = 
		_UniqueDeploymentConfigAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _UniqueDeploymentConfigAccess(override val accessCondition: Option[Condition]) 
		extends UniqueDeploymentConfigAccess
}

/**
  * A common trait for access points that return individual and distinct deployment configs.
  * @author Mikko Hilpinen
  * @since 12.08.2024, v1.2
  */
trait UniqueDeploymentConfigAccess 
	extends UniqueDeploymentConfigAccessLike[DeploymentConfig, UniqueDeploymentConfigAccess] 
		with SingleRowModelAccess[DeploymentConfig] with NullDeprecatableView[UniqueDeploymentConfigAccess]
{
	// IMPLEMENTED	--------------------
	
	override def factory = DeploymentConfigDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): UniqueDeploymentConfigAccess = 
		UniqueDeploymentConfigAccess(condition)
}

