package vf.optidepy.database.access.many.dependency

import utopia.vault.database.Connection
import utopia.vault.nosql.access.many.model.ManyRowModelAccess
import utopia.vault.nosql.view.ViewFactory
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.dependency.UpdatedDependencyDbFactory
import vf.optidepy.database.storable.dependency.DependencyUpdateDbModel
import vf.optidepy.model.combined.dependency.UpdatedDependency

object ManyUpdatedDependenciesAccess extends ViewFactory[ManyUpdatedDependenciesAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): ManyUpdatedDependenciesAccess = 
		_ManyUpdatedDependenciesAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _ManyUpdatedDependenciesAccess(override val accessCondition: Option[Condition]) 
		extends ManyUpdatedDependenciesAccess
}

/**
  * A common trait for access points that return multiple updated dependencies at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024
  */
trait ManyUpdatedDependenciesAccess 
	extends ManyDependenciesAccessLike[UpdatedDependency, ManyUpdatedDependenciesAccess] 
		with ManyRowModelAccess[UpdatedDependency]
{
	// COMPUTED	--------------------
	
	/**
	  * dependency ids of the accessible dependency updates
	  */
	def updateDependencyIds(implicit connection: Connection) = 
		pullColumn(updateModel.dependencyId.column).map { v => v.getInt }
	
	/**
	  * release ids of the accessible dependency updates
	  */
	def updateReleaseIds(implicit connection: Connection) = 
		pullColumn(updateModel.releaseId.column).map { v => v.getInt }
	
	/**
	  * creation times of the accessible dependency updates
	  */
	def updateCreationTimes(implicit connection: Connection) = 
		pullColumn(updateModel.created.column).map { v => v.getInstant }
	
	/**
	  * Model (factory) used for interacting the dependency updates associated with this updated dependency
	  */
	protected def updateModel = DependencyUpdateDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = UpdatedDependencyDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): ManyUpdatedDependenciesAccess = 
		ManyUpdatedDependenciesAccess(condition)
}

