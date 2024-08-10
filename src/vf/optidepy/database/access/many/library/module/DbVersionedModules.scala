package vf.optidepy.database.access.many.library.module

import utopia.vault.nosql.view.{NonDeprecatedView, UnconditionalView, ViewManyByIntIds}
import vf.optidepy.model.stored.library.VersionedModule

/**
  * The root access point when targeting multiple versioned modules at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbVersionedModules 
	extends ManyVersionedModulesAccess with NonDeprecatedView[VersionedModule] 
		with ViewManyByIntIds[ManyVersionedModulesAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * A copy of this access point that includes historical (i.e. deprecated) versioned modules
	  */
	def includingHistory = DbVersionedModulesIncludingHistory
	
	
	// NESTED	--------------------
	
	object DbVersionedModulesIncludingHistory extends ManyVersionedModulesAccess with UnconditionalView
}

