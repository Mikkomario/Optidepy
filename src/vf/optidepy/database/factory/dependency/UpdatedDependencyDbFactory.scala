package vf.optidepy.database.factory.dependency

import utopia.vault.nosql.factory.row.linked.PossiblyCombiningFactory
import utopia.vault.nosql.template.Deprecatable
import vf.optidepy.model.combined.dependency.UpdatedDependency
import vf.optidepy.model.stored.dependency.{Dependency, DependencyUpdate}

/**
  * Used for reading updated dependencies from the database
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object UpdatedDependencyDbFactory 
	extends PossiblyCombiningFactory[UpdatedDependency, Dependency, DependencyUpdate] with Deprecatable
{
	// IMPLEMENTED	--------------------
	
	override def childFactory = DependencyUpdateDbFactory
	
	override def nonDeprecatedCondition = parentFactory.nonDeprecatedCondition
	
	override def parentFactory = DependencyDbFactory
	
	override def apply(dependency: Dependency, update: Option[DependencyUpdate]) = 
		UpdatedDependency(dependency, update)
}

