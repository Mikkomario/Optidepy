package vf.optidepy.database.access.single.dependency

import utopia.vault.database.Connection
import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.view.{NullDeprecatableView, ViewFactory}
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.dependency.UpdatedDependencyDbFactory
import vf.optidepy.database.storable.dependency.DependencyUpdateDbModel
import vf.optidepy.model.combined.dependency.UpdatedDependency

object UniqueUpdatedDependencyAccess extends ViewFactory[UniqueUpdatedDependencyAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): UniqueUpdatedDependencyAccess = 
		_UniqueUpdatedDependencyAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _UniqueUpdatedDependencyAccess(override val accessCondition: Option[Condition]) 
		extends UniqueUpdatedDependencyAccess
}

/**
  * A common trait for access points that return distinct updated dependencies
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait UniqueUpdatedDependencyAccess 
	extends UniqueDependencyAccessLike[UpdatedDependency, UniqueUpdatedDependencyAccess] 
		with SingleRowModelAccess[UpdatedDependency] with NullDeprecatableView[UniqueUpdatedDependencyAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * Id of the dependency this update concerns. 
	  * None if no dependency update (or value) was found.
	  */
	def updateDependencyId(implicit connection: Connection) = pullColumn(updateModel.dependencyId.column).int
	
	/**
	  * Id of the library release that was to the parent project. 
	  * None if no dependency update (or value) was found.
	  */
	def updateReleaseId(implicit connection: Connection) = pullColumn(updateModel.releaseId.column).int
	
	/**
	  * Time when this update was made. 
	  * None if no dependency update (or value) was found.
	  */
	def updateCreated(implicit connection: Connection) = pullColumn(updateModel.created.column).instant
	
	/**
	  * A database model (factory) used for interacting with the linked update
	  */
	protected def updateModel = DependencyUpdateDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = UpdatedDependencyDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): UniqueUpdatedDependencyAccess = 
		UniqueUpdatedDependencyAccess(condition)
}

