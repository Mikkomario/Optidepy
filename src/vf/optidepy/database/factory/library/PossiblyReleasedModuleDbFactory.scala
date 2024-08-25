package vf.optidepy.database.factory.library

import utopia.vault.nosql.factory.row.FromTimelineRowFactory
import utopia.vault.nosql.factory.row.linked.PossiblyCombiningFactory
import utopia.vault.nosql.template.Deprecatable
import vf.optidepy.model.combined.library.PossiblyReleasedModule
import vf.optidepy.model.stored.library.{ModuleRelease, VersionedModule}

/**
  * Used for reading possibly released modules from the database
  * @author Mikko Hilpinen
  * @since 24.08.2024, v1.2
  */
object PossiblyReleasedModuleDbFactory 
	extends PossiblyCombiningFactory[PossiblyReleasedModule, VersionedModule, ModuleRelease] 
		with FromTimelineRowFactory[PossiblyReleasedModule] with Deprecatable
{
	// IMPLEMENTED	--------------------
	
	override def childFactory = ModuleReleaseDbFactory
	
	override def nonDeprecatedCondition = parentFactory.nonDeprecatedCondition
	
	override def parentFactory = VersionedModuleDbFactory
	
	override def timestamp = parentFactory.timestamp
	
	/**
	  * @param module module to wrap
	  * @param release release to attach to this module
	  */
	override def apply(module: VersionedModule, release: Option[ModuleRelease]) = 
		PossiblyReleasedModule(module, release)
}

