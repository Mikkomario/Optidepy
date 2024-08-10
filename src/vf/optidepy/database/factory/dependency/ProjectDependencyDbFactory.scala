package vf.optidepy.database.factory.dependency

import utopia.vault.nosql.factory.row.linked.PossiblyCombiningFactory
import utopia.vault.nosql.template.Deprecatable
import vf.optidepy.database.factory.project.ProjectDbFactory
import vf.optidepy.model.combined.dependency.ProjectDependency
import vf.optidepy.model.stored.dependency.Dependency
import vf.optidepy.model.stored.project.Project

/**
  * Used for reading project dependencies from the database
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object ProjectDependencyDbFactory 
	extends PossiblyCombiningFactory[ProjectDependency, Dependency, Project] with Deprecatable
{
	// IMPLEMENTED	--------------------
	
	override def childFactory = ProjectDbFactory
	
	override def nonDeprecatedCondition = 
		parentFactory.nonDeprecatedCondition && childFactory.nonDeprecatedCondition
	
	override def parentFactory = DependencyDbFactory
	
	override def apply(dependency: Dependency, project: Option[Project]) = ProjectDependency(dependency, 
		project)
}

