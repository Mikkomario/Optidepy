package vf.optidepy.model.combined.library

import vf.optidepy.model.stored.library.{ModuleRelease, VersionedModule}

object PossiblyReleasedModule
{
	// OTHER	--------------------
	
	/**
	  * @param module module to wrap
	  * @param release release to attach to this module
	  * @return Combination of the specified module and release
	  */
	def apply(module: VersionedModule, release: Option[ModuleRelease]): PossiblyReleasedModule = 
		_PossiblyReleasedModule(module, release)
	
	
	// NESTED	--------------------
	
	/**
	  * @param module module to wrap
	  * @param release release to attach to this module
	  */
	private case class _PossiblyReleasedModule(module: VersionedModule, release: Option[ModuleRelease]) 
		extends PossiblyReleasedModule
	{
		// IMPLEMENTED	--------------------
		
		override protected def wrap(factory: VersionedModule) = copy(module = factory)
	}
}

/**
  * Attaches a release to a module, but only if available.
  * @author Mikko Hilpinen
  * @since 24.08.2024, v1.2
  */
trait PossiblyReleasedModule extends CombinedVersionedModule[PossiblyReleasedModule]
{
	// ABSTRACT	--------------------
	
	/**
	  * Wrapped versioned module
	  */
	def module: VersionedModule
	
	/**
	  * The release that is attached to this module
	  */
	def release: Option[ModuleRelease]
	
	
	// IMPLEMENTED	--------------------
	
	override def versionedModule = module
}

