package vf.optidepy.model.combined.dependency

import utopia.flow.view.template.Extender
import utopia.vault.model.template.HasId
import vf.optidepy.model.factory.dependency.DependencyFactoryWrapper
import vf.optidepy.model.partial.dependency.DependencyData
import vf.optidepy.model.stored.dependency.Dependency
import vf.optidepy.model.stored.project.Project

/**
  * Combines project and dependency information
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class ProjectDependency(dependency: Dependency, project: Option[Project]) 
	extends Extender[DependencyData] with HasId[Int] with DependencyFactoryWrapper[Dependency, ProjectDependency]
{
	// COMPUTED	--------------------
	
	/**
	  * Id of this dependency in the database
	  */
	def id = dependency.id
	
	
	// IMPLEMENTED	--------------------
	
	override def wrapped = dependency.data
	
	override protected def wrappedFactory = dependency
	
	override protected def wrap(factory: Dependency) = copy(dependency = factory)
}

