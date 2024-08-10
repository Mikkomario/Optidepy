package vf.optidepy.database.access.many.deployment.config

import utopia.vault.nosql.access.many.model.ManyRowModelAccess
import utopia.vault.nosql.view.ViewFactory
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.deployment.DeploymentConfigDbFactory
import vf.optidepy.model.stored.deployment.DeploymentConfig

object ManyDeploymentConfigsAccess extends ViewFactory[ManyDeploymentConfigsAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): ManyDeploymentConfigsAccess = 
		_ManyDeploymentConfigsAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _ManyDeploymentConfigsAccess(override val accessCondition: Option[Condition]) 
		extends ManyDeploymentConfigsAccess
}

/**
  * A common trait for access points which target multiple deployment configs at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait ManyDeploymentConfigsAccess 
	extends ManyDeploymentConfigsAccessLike[DeploymentConfig, ManyDeploymentConfigsAccess] 
		with ManyRowModelAccess[DeploymentConfig]
{
	// IMPLEMENTED	--------------------
	
	override def factory = DeploymentConfigDbFactory
	
	override protected def self = this
	
	override
		 def apply(condition: Condition): ManyDeploymentConfigsAccess = ManyDeploymentConfigsAccess(condition)
}

