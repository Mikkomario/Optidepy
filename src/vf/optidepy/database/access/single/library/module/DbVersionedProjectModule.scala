package vf.optidepy.database.access.single.library.module

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.NonDeprecatedView
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.VersionedProjectModuleDbFactory
import vf.optidepy.database.storable.library.VersionedModuleDbModel
import vf.optidepy.database.storable.project.ProjectDbModel
import vf.optidepy.model.combined.library.VersionedProjectModule

/**
  * Used for accessing individual versioned project modules
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbVersionedProjectModule 
	extends SingleRowModelAccess[VersionedProjectModule] with NonDeprecatedView[VersionedProjectModule] 
		with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * A database model (factory) used for interacting with the linked project
	  */
	protected def projectModel = ProjectDbModel
	
	/**
	  * A database model (factory) used for interacting with linked modules
	  */
	private def model = VersionedModuleDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = VersionedProjectModuleDbFactory
	
	
	// OTHER	--------------------
	
	/**
	  * @param id Database id of the targeted versioned project module
	  * @return An access point to that versioned project module
	  */
	def apply(id: Int) = DbSingleVersionedProjectModule(id)
	
	/**
	  * 
		@param additionalCondition Filter condition to apply in addition to this root view's condition. Should yield
	  *  unique versioned project modules.
	  * @return An access point to the versioned project module that satisfies the specified condition
	  */
	protected def filterDistinct(additionalCondition: Condition) = 
		UniqueVersionedProjectModuleAccess(mergeCondition(additionalCondition))
}

