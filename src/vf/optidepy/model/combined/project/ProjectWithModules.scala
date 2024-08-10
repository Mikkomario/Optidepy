package vf.optidepy.model.combined.project

import vf.optidepy.model.stored.library.VersionedModule
import vf.optidepy.model.stored.project.Project

object ProjectWithModules
{
	// OTHER	--------------------
	
	/**
	  * @param project project to wrap
	  * @param modules modules to attach to this project
	  * @return Combination of the specified project and module
	  */
	def apply(project: Project, modules: Seq[VersionedModule]): ProjectWithModules =
		_ProjectWithModules(project, modules)
	
	
	// NESTED	--------------------
	
	/**
	  * @param project project to wrap
	  * @param modules module to attach to this project
	  */
	private case class _ProjectWithModules(project: Project, modules: Seq[VersionedModule])
		extends ProjectWithModules
	{
		// IMPLEMENTED	--------------------
		
		override protected def wrap(factory: Project) = copy(project = factory)
	}
}

/**
  * Includes versioned modules to a project
  * @author Mikko Hilpinen
  * @since 10.08.2024, v1.2
  */
trait ProjectWithModules extends CombinedProject[ProjectWithModules]
{
	// ABSTRACT	--------------------
	
	/**
	  * Module that are attached to this project
	  */
	def modules: Seq[VersionedModule]
}

