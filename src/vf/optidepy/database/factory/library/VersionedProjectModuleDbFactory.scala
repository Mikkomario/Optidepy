package vf.optidepy.database.factory.library

import utopia.vault.nosql.factory.row.FromTimelineRowFactory
import utopia.vault.nosql.factory.row.linked.PossiblyCombiningFactory
import utopia.vault.nosql.template.Deprecatable
import vf.optidepy.database.factory.project.ProjectDbFactory
import vf.optidepy.model.combined.library.VersionedProjectModule
import vf.optidepy.model.stored.library.VersionedModule
import vf.optidepy.model.stored.project.Project

/**
  * Used for reading versioned project modules from the database
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object VersionedProjectModuleDbFactory 
	extends PossiblyCombiningFactory[VersionedProjectModule, VersionedModule, Project] 
		with FromTimelineRowFactory[VersionedProjectModule] with Deprecatable
{
	// IMPLEMENTED	--------------------
	
	override def childFactory = ProjectDbFactory
	
	override def nonDeprecatedCondition = 
		parentFactory.nonDeprecatedCondition && childFactory.nonDeprecatedCondition
	
	override def parentFactory = VersionedModuleDbFactory
	
	override def timestamp = parentFactory.timestamp
	
	override def apply(module: VersionedModule, project: Option[Project]) = 
		VersionedProjectModule(module, project)
}

