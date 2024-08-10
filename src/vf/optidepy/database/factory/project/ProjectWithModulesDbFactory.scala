package vf.optidepy.database.factory.project

import utopia.vault.nosql.factory.multi.MultiCombiningFactory
import utopia.vault.nosql.template.Deprecatable
import vf.optidepy.database.factory.library.VersionedModuleDbFactory
import vf.optidepy.model.combined.project.ProjectWithModules
import vf.optidepy.model.stored.library.VersionedModule
import vf.optidepy.model.stored.project.Project

/**
  * Used for reading projects with modules from the database
  * @author Mikko Hilpinen
  * @since 10.08.2024, v1.2
  */
object ProjectWithModulesDbFactory 
	extends MultiCombiningFactory[ProjectWithModules, Project, VersionedModule] with Deprecatable
{
	// IMPLEMENTED	--------------------
	
	override def childFactory = VersionedModuleDbFactory
	
	override def isAlwaysLinked = true
	
	override def nonDeprecatedCondition = 
		parentFactory.nonDeprecatedCondition && childFactory.nonDeprecatedCondition
	
	override def parentFactory = ProjectDbFactory
	
	/**
	  * @param project project to wrap
	  * @param module module to attach to this project
	  */
	override def apply(project: Project, module: Seq[VersionedModule]) = ProjectWithModules(project, module)
}

