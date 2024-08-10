package vf.optidepy.database.access.single.dependency.update

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.UnconditionalView
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.dependency.DependencyUpdateDbFactory
import vf.optidepy.database.storable.dependency.DependencyUpdateDbModel
import vf.optidepy.model.stored.dependency.DependencyUpdate

/**
  * Used for accessing individual dependency updates
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbDependencyUpdate extends SingleRowModelAccess[DependencyUpdate] with UnconditionalView with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	protected def model = DependencyUpdateDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = DependencyUpdateDbFactory
	
	
	// OTHER	--------------------
	
	/**
	  * @param id Database id of the targeted dependency update
	  * @return An access point to that dependency update
	  */
	def apply(id: Int) = DbSingleDependencyUpdate(id)
	
	/**
	  * @param condition Filter condition to apply in addition to this root view's condition. Should yield
	  *  unique dependency updates.
	  * @return An access point to the dependency update that satisfies the specified condition
	  */
	protected def distinct(condition: Condition) = UniqueDependencyUpdateAccess(condition)
}

