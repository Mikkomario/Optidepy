package vf.optidepy.database.access.many.dependency

import utopia.vault.nosql.view.{NonDeprecatedView, UnconditionalView, ViewManyByIntIds}
import vf.optidepy.model.stored.dependency.Dependency

/**
  * The root access point when targeting multiple dependencies at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbDependencies 
	extends ManyDependenciesAccess with NonDeprecatedView[Dependency] 
		with ViewManyByIntIds[ManyDependenciesAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * A copy of this access point that includes historical (i.e. deprecated) dependencies
	  */
	def includingHistory = DbDependenciesIncludingHistory
	
	
	// NESTED	--------------------
	
	object DbDependenciesIncludingHistory extends ManyDependenciesAccess with UnconditionalView
}

