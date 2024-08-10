package vf.optidepy.database.access.many.library.module

import utopia.vault.nosql.view.{NonDeprecatedView, UnconditionalView, ViewManyByIntIds}
import vf.optidepy.model.combined.library.VersionedProjectModule

/**
  * The root access point when targeting multiple versioned project modules at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbVersionedProjectModules 
	extends ManyVersionedProjectModulesAccess with NonDeprecatedView[VersionedProjectModule] 
		with ViewManyByIntIds[ManyVersionedProjectModulesAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * A copy of this access point that includes historical (i.e. deprecated) versioned project modules
	  */
	def includingHistory = DbVersionedProjectModulesIncludingHistory
	
	
	// NESTED	--------------------
	
	object DbVersionedProjectModulesIncludingHistory 
		extends ManyVersionedProjectModulesAccess with UnconditionalView
}

