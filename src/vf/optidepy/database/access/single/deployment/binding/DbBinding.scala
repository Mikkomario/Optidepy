package vf.optidepy.database.access.single.deployment.binding

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.UnconditionalView
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.deployment.BindingDbFactory
import vf.optidepy.database.storable.deployment.BindingDbModel
import vf.optidepy.model.stored.deployment.Binding

/**
  * Used for accessing individual bindings
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbBinding extends SingleRowModelAccess[Binding] with UnconditionalView with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	protected def model = BindingDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = BindingDbFactory
	
	
	// OTHER	--------------------
	
	/**
	  * @param id Database id of the targeted binding
	  * @return An access point to that binding
	  */
	def apply(id: Int) = DbSingleBinding(id)
	
	/**
	  * @param condition Filter condition to apply in addition to this root view's condition. Should yield
	  *  unique bindings.
	  * @return An access point to the binding that satisfies the specified condition
	  */
	protected def distinct(condition: Condition) = UniqueBindingAccess(condition)
}

