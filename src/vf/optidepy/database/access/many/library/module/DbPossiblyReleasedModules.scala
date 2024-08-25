package vf.optidepy.database.access.many.library.module

import utopia.vault.nosql.view.{NonDeprecatedView, UnconditionalView, ViewManyByIntIds}
import vf.optidepy.model.combined.library.PossiblyReleasedModule

/**
  * The root access point when targeting multiple possibly released modules at a time
  * @author Mikko Hilpinen
  * @since 24.08.2024, v1.2
  */
object DbPossiblyReleasedModules 
	extends ManyPossiblyReleasedModulesAccess with NonDeprecatedView[PossiblyReleasedModule] 
		with ViewManyByIntIds[ManyPossiblyReleasedModulesAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * A copy of this access point that includes historical (i.e. deprecated) possibly released modules
	  */
	def includingHistory = DbPossiblyReleasedModulesIncludingHistory
	
	
	// NESTED	--------------------
	
	object DbPossiblyReleasedModulesIncludingHistory 
		extends ManyPossiblyReleasedModulesAccess with UnconditionalView
}

