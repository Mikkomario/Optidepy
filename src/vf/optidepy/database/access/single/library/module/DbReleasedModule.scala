package vf.optidepy.database.access.single.library.module

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.NonDeprecatedView
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.ReleasedModuleDbFactory
import vf.optidepy.database.storable.library.{ModuleReleaseDbModel, VersionedModuleDbModel}
import vf.optidepy.model.combined.library.ReleasedModule

/**
  * Used for accessing individual released modules
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbReleasedModule 
	extends SingleRowModelAccess[ReleasedModule] with NonDeprecatedView[ReleasedModule] with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * A database model (factory) used for interacting with linked modules
	  */
	protected def model = VersionedModuleDbModel
	
	/**
	  * A database model (factory) used for interacting with the linked release
	  */
	protected def releaseModel = ModuleReleaseDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = ReleasedModuleDbFactory
	
	
	// OTHER	--------------------
	
	/**
	  * @param id Database id of the targeted released module
	  * @return An access point to that released module
	  */
	def apply(id: Int) = DbSingleReleasedModule(id)
	
	/**
	  * 
		@param additionalCondition Filter condition to apply in addition to this root view's condition. Should yield
	  *  unique released modules.
	  * @return An access point to the released module that satisfies the specified condition
	  */
	protected def filterDistinct(additionalCondition: Condition) = 
		UniqueReleasedModuleAccess(mergeCondition(additionalCondition))
}

