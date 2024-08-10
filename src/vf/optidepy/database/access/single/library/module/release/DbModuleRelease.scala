package vf.optidepy.database.access.single.library.module.release

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.UnconditionalView
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.ModuleReleaseDbFactory
import vf.optidepy.database.storable.library.ModuleReleaseDbModel
import vf.optidepy.model.stored.library.ModuleRelease

/**
  * Used for accessing individual module releases
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbModuleRelease extends SingleRowModelAccess[ModuleRelease] with UnconditionalView with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	protected def model = ModuleReleaseDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = ModuleReleaseDbFactory
	
	
	// OTHER	--------------------
	
	/**
	  * @param id Database id of the targeted module release
	  * @return An access point to that module release
	  */
	def apply(id: Int) = DbSingleModuleRelease(id)
	
	/**
	  * @param condition Filter condition to apply in addition to this root view's condition. Should yield
	  *  unique module releases.
	  * @return An access point to the module release that satisfies the specified condition
	  */
	protected def distinct(condition: Condition) = UniqueModuleReleaseAccess(condition)
}

