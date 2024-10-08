package vf.optidepy.model.combined.library

import vf.optidepy.model.stored.library.{ModuleRelease, VersionedModule}

object ReleasedModule
{
	// OTHER	--------------------
	
	/**
	  * @param module module to wrap
	  * @param release release to attach to this module
	  * @return Combination of the specified module and release
	  */
	def apply(module: VersionedModule, release: ModuleRelease): ReleasedModule = _ReleasedModule(module, 
		release)
	
	
	// NESTED	--------------------
	
	/**
	  * @param module module to wrap
	  * @param release release to attach to this module
	  */
	private case class _ReleasedModule(module: VersionedModule, release: ModuleRelease) extends ReleasedModule
	{
		// IMPLEMENTED	--------------------
		
		override protected def wrap(factory: VersionedModule) = copy(module = factory)
	}
}

/**
  * Includes a single release's information in a module
  * @author Mikko Hilpinen
  * @since 24.08.2024, v1.2
  */
trait ReleasedModule extends CombinedVersionedModule[ReleasedModule]
{
	// ABSTRACT	--------------------
	
	/**
	  * Wrapped versioned module
	  */
	def module: VersionedModule
	/**
	  * The release that is attached to this module
	  */
	def release: ModuleRelease
	
	
	// IMPLEMENTED	--------------------
	
	override def versionedModule = module
}
