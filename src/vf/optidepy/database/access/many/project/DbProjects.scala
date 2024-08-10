package vf.optidepy.database.access.many.project

import utopia.vault.nosql.view.{NonDeprecatedView, UnconditionalView, ViewManyByIntIds}
import vf.optidepy.model.stored.project.Project

/**
  * The root access point when targeting multiple projects at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbProjects 
	extends ManyProjectsAccess with NonDeprecatedView[Project] with ViewManyByIntIds[ManyProjectsAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * A copy of this access point that includes historical (i.e. deprecated) projects
	  */
	def includingHistory = DbProjectsIncludingHistory
	
	
	// NESTED	--------------------
	
	object DbProjectsIncludingHistory extends ManyProjectsAccess with UnconditionalView
}

