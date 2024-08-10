package vf.optidepy.database.access.many.dependency

import utopia.vault.nosql.view.{NonDeprecatedView, UnconditionalView, ViewManyByIntIds}
import vf.optidepy.model.combined.dependency.ProjectDependency

/**
  * The root access point when targeting multiple project dependencies at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbProjectDependencies 
	extends ManyProjectDependenciesAccess with NonDeprecatedView[ProjectDependency] 
		with ViewManyByIntIds[ManyProjectDependenciesAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * A copy of this access point that includes historical (i.e. deprecated) project dependencies
	  */
	def includingHistory = DbProjectDependenciesIncludingHistory
	
	
	// NESTED	--------------------
	
	object DbProjectDependenciesIncludingHistory extends ManyProjectDependenciesAccess with UnconditionalView
}

