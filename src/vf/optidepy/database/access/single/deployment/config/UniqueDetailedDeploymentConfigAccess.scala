package vf.optidepy.database.access.single.deployment.config

import utopia.vault.nosql.view.{NullDeprecatableView, ViewFactory}
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.deployment.DetailedDeploymentConfigDbFactory
import vf.optidepy.database.storable.deployment.BindingDbModel
import vf.optidepy.model.combined.deployment.DetailedDeploymentConfig

object UniqueDetailedDeploymentConfigAccess extends ViewFactory[UniqueDetailedDeploymentConfigAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): UniqueDetailedDeploymentConfigAccess = 
		_UniqueDetailedDeploymentConfigAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _UniqueDetailedDeploymentConfigAccess(override val accessCondition: Option[Condition]) 
		extends UniqueDetailedDeploymentConfigAccess
}

/**
  * A common trait for access points that return distinct detailed deployment configs
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait UniqueDetailedDeploymentConfigAccess 
	extends UniqueDeploymentConfigAccessLike[DetailedDeploymentConfig, UniqueDetailedDeploymentConfigAccess] 
		with NullDeprecatableView[UniqueDetailedDeploymentConfigAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * A database model (factory) used for interacting with the linked binding
	  */
	protected def bindingModel = BindingDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = DetailedDeploymentConfigDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): UniqueDetailedDeploymentConfigAccess = 
		UniqueDetailedDeploymentConfigAccess(condition)
}

