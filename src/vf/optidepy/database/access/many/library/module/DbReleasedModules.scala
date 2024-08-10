package vf.optidepy.database.access.many.library.module

import utopia.vault.nosql.view.{NonDeprecatedView, UnconditionalView, ViewManyByIntIds}
import vf.optidepy.model.combined.library.ReleasedModule

/**
  * The root access point when targeting multiple released modules at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbReleasedModules 
	extends ManyReleasedModulesAccess with NonDeprecatedView[ReleasedModule] 
		with ViewManyByIntIds[ManyReleasedModulesAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * A copy of this access point that includes historical (i.e. deprecated) released modules
	  */
	def includingHistory = DbReleasedModulesIncludingHistory
	
	
	// NESTED	--------------------
	
	object DbReleasedModulesIncludingHistory extends ManyReleasedModulesAccess with UnconditionalView
}

