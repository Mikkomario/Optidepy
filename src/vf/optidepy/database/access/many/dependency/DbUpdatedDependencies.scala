package vf.optidepy.database.access.many.dependency

import utopia.vault.nosql.view.{NonDeprecatedView, UnconditionalView, ViewManyByIntIds}
import vf.optidepy.model.combined.dependency.UpdatedDependency

/**
  * The root access point when targeting multiple updated dependencies at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbUpdatedDependencies 
	extends ManyUpdatedDependenciesAccess with NonDeprecatedView[UpdatedDependency] 
		with ViewManyByIntIds[ManyUpdatedDependenciesAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * A copy of this access point that includes historical (i.e. deprecated) updated dependencies
	  */
	def includingHistory = DbUpdatedDependenciesIncludingHistory
	
	
	// NESTED	--------------------
	
	object DbUpdatedDependenciesIncludingHistory extends ManyUpdatedDependenciesAccess with UnconditionalView
}

