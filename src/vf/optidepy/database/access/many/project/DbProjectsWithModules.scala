package vf.optidepy.database.access.many.project

import utopia.vault.nosql.view.{NonDeprecatedView, UnconditionalView, ViewManyByIntIds}
import vf.optidepy.model.combined.project.ProjectWithModules

/**
  * The root access point when targeting multiple projects with modules at a time
  * @author Mikko Hilpinen
  * @since 10.08.2024, v1.2
  */
object DbProjectsWithModules 
	extends ManyProjectsWithModulesAccess with NonDeprecatedView[ProjectWithModules] 
		with ViewManyByIntIds[ManyProjectsWithModulesAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * A copy of this access point that includes historical (i.e. deprecated) projects with modules
	  */
	def includingHistory = DbProjectsWithModulesIncludingHistory
	
	
	// NESTED	--------------------
	
	object DbProjectsWithModulesIncludingHistory extends ManyProjectsWithModulesAccess with UnconditionalView
}

