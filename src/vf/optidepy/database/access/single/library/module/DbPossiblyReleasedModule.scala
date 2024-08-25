package vf.optidepy.database.access.single.library.module

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.NonDeprecatedView
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.PossiblyReleasedModuleDbFactory
import vf.optidepy.database.storable.library.{ModuleReleaseDbModel, VersionedModuleDbModel}
import vf.optidepy.model.combined.library.PossiblyReleasedModule

/**
  * Used for accessing individual possibly released modules
  * @author Mikko Hilpinen
  * @since 24.08.2024, v1.2
  */
object DbPossiblyReleasedModule 
	extends SingleRowModelAccess[PossiblyReleasedModule] with NonDeprecatedView[PossiblyReleasedModule] 
		with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * A database model (factory) used for interacting with the linked release
	  */
	protected def releaseModel = ModuleReleaseDbModel
	
	/**
	  * A database model (factory) used for interacting with linked modules
	  */
	private def model = VersionedModuleDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = PossiblyReleasedModuleDbFactory
	
	
	// OTHER	--------------------
	
	/**
	  * @param id Database id of the targeted possibly released module
	  * @return An access point to that possibly released module
	  */
	def apply(id: Int) = DbSinglePossiblyReleasedModule(id)
	
	/**
	  * 
		@param additionalCondition Filter condition to apply in addition to this root view's condition. Should yield
	  *  unique possibly released modules.
	  * @return An access point to the possibly released module that satisfies the specified condition
	  */
	private def filterDistinct(additionalCondition: Condition) = 
		UniquePossiblyReleasedModuleAccess(mergeCondition(additionalCondition))
}

