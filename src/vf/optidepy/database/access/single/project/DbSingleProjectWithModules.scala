package vf.optidepy.database.access.single.project

import utopia.vault.nosql.access.single.model.distinct.SingleIntIdModelAccess
import vf.optidepy.model.combined.project.ProjectWithModules

/**
  * An access point to individual projects with modules, based on their project id
  * @author Mikko Hilpinen
  * @since 10.08.2024, v1.2
  */
case class DbSingleProjectWithModules(id: Int) 
	extends UniqueProjectWithModulesAccess with SingleIntIdModelAccess[ProjectWithModules]

