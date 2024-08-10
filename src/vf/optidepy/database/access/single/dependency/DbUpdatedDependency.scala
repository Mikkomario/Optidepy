package vf.optidepy.database.access.single.dependency

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.NonDeprecatedView
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.dependency.UpdatedDependencyDbFactory
import vf.optidepy.database.storable.dependency.{DependencyDbModel, DependencyUpdateDbModel}
import vf.optidepy.model.combined.dependency.UpdatedDependency

/**
  * Used for accessing individual updated dependencies
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbUpdatedDependency 
	extends SingleRowModelAccess[UpdatedDependency] with NonDeprecatedView[UpdatedDependency] with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * A database model (factory) used for interacting with the linked update
	  */
	protected def updateModel = DependencyUpdateDbModel
	
	/**
	  * A database model (factory) used for interacting with linked dependencies
	  */
	private def model = DependencyDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = UpdatedDependencyDbFactory
	
	
	// OTHER	--------------------
	
	/**
	  * @param id Database id of the targeted updated dependency
	  * @return An access point to that updated dependency
	  */
	def apply(id: Int) = DbSingleUpdatedDependency(id)
	
	/**
	  * 
		@param additionalCondition Filter condition to apply in addition to this root view's condition. Should yield
	  *  unique updated dependencies.
	  * @return An access point to the updated dependency that satisfies the specified condition
	  */
	protected def filterDistinct(additionalCondition: Condition) = 
		UniqueUpdatedDependencyAccess(mergeCondition(additionalCondition))
}

