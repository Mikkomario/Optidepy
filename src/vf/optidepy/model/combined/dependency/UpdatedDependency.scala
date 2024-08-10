package vf.optidepy.model.combined.dependency

import utopia.flow.view.template.Extender
import utopia.vault.model.template.HasId
import vf.optidepy.model.factory.dependency.DependencyFactoryWrapper
import vf.optidepy.model.partial.dependency.DependencyData
import vf.optidepy.model.stored.dependency.{Dependency, DependencyUpdate}

/**
  * Represents a dependency that has been updated at some point
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class UpdatedDependency(dependency: Dependency, update: Option[DependencyUpdate]) 
	extends Extender[DependencyData] with HasId[Int] with DependencyFactoryWrapper[Dependency, UpdatedDependency]
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

