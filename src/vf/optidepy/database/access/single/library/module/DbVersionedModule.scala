package vf.optidepy.database.access.single.library.module

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.NonDeprecatedView
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.VersionedModuleDbFactory
import vf.optidepy.database.storable.library.VersionedModuleDbModel
import vf.optidepy.model.stored.library.VersionedModule

/**
  * Used for accessing individual versioned modules
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbVersionedModule 
	extends SingleRowModelAccess[VersionedModule] with NonDeprecatedView[VersionedModule] with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	protected def model = VersionedModuleDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = VersionedModuleDbFactory
	
	
	// OTHER	--------------------
	
	/**
	  * @param id Database id of the targeted versioned module
	  * @return An access point to that versioned module
	  */
	def apply(id: Int) = DbSingleVersionedModule(id)
	
	/**
	  * 
		@param additionalCondition Filter condition to apply in addition to this root view's condition. Should yield
	  *  unique versioned modules.
	  * @return An access point to the versioned module that satisfies the specified condition
	  */
	protected def filterDistinct(additionalCondition: Condition) = 
		UniqueVersionedModuleAccess(mergeCondition(additionalCondition))
}

