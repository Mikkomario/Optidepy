package vf.optidepy.model.combined.library

import utopia.flow.parse.file.FileExtensions._
import vf.optidepy.model.stored.library.VersionedModule
import vf.optidepy.model.stored.project.Project

object VersionedProjectModule
{
	// OTHER	--------------------
	
	/**
	  * @param module module to wrap
	  * @param project project to attach to this module
	  * @return Combination of the specified module and project
	  */
	def apply(module: VersionedModule, project: Project): VersionedProjectModule = 
		_VersionedProjectModule(module, project)
	
	
	// NESTED	--------------------
	
	/**
	  * @param module module to wrap
	  * @param project project to attach to this module
	  */
	private case class _VersionedProjectModule(module: VersionedModule, project: Project) 
		extends VersionedProjectModule
	{
		// IMPLEMENTED	--------------------
		
		override protected def wrap(factory: VersionedModule) = copy(module = factory)
	}
}

/**
  * Combines project and module information
  * @author Mikko Hilpinen
  * @since 24.08.2024, v1.2
  */
trait VersionedProjectModule extends CombinedVersionedModule[VersionedProjectModule]
{
	// ABSTRACT	--------------------
	
	/**
	  * Wrapped versioned module
	  */
	def module: VersionedModule
	/**
	  * The project that is attached to this module
	  */
	def project: Project
	
	
	// COMPUTED ------------------------
	
	/**
	 * @return Path to the version / change documentation for this module
	 */
	def changeListPath = project.rootPath/module.relativeChangeListPath
	/**
	 * @return Path to the directory where this module's jar files are placed
	 */
	def artifactDirectory = project.rootPath/module.relativeArtifactDirectory
	
	
	// IMPLEMENTED	--------------------
	
	override def versionedModule = module
	
	override def toString = {
		if (module.name.isEmpty || module.name == project.name)
			project.name
		else
			s"${ project.name }/${ module.name }"
	}
}