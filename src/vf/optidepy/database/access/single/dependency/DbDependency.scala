package vf.optidepy.database.access.single.dependency

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.NonDeprecatedView
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.dependency.DependencyDbFactory
import vf.optidepy.database.storable.dependency.DependencyDbModel
import vf.optidepy.model.stored.dependency.Dependency

/**
  * Used for accessing individual dependencies
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbDependency extends SingleRowModelAccess[Dependency] with NonDeprecatedView[Dependency] with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	private def model = DependencyDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = DependencyDbFactory
	
	
	// OTHER	--------------------
	
	/**
	  * @param id Database id of the targeted dependency
	  * @return An access point to that dependency
	  */
	def apply(id: Int) = DbSingleDependency(id)
	
	/**
	  * 
		@param additionalCondition Filter condition to apply in addition to this root view's condition. Should yield
	  *  unique dependencies.
	  * @return An access point to the dependency that satisfies the specified condition
	  */
	protected def filterDistinct(additionalCondition: Condition) = 
		UniqueDependencyAccess(mergeCondition(additionalCondition))
}

