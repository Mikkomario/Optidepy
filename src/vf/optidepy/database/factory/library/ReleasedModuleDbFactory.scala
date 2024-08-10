package vf.optidepy.database.factory.library

import utopia.vault.nosql.factory.row.FromTimelineRowFactory
import utopia.vault.nosql.factory.row.linked.CombiningFactory
import utopia.vault.nosql.template.Deprecatable
import vf.optidepy.model.combined.library.ReleasedModule
import vf.optidepy.model.stored.library.{ModuleRelease, VersionedModule}

/**
  * Used for reading released modules from the database
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object ReleasedModuleDbFactory 
	extends CombiningFactory[ReleasedModule, VersionedModule, ModuleRelease] 
		with FromTimelineRowFactory[ReleasedModule] with Deprecatable
{
	// IMPLEMENTED	--------------------
	
	override def childFactory = ModuleReleaseDbFactory
	
	override def nonDeprecatedCondition = parentFactory.nonDeprecatedCondition
	
	override def parentFactory = VersionedModuleDbFactory
	
	override def timestamp = parentFactory.timestamp
	
	override def apply(module: VersionedModule, release: ModuleRelease) = ReleasedModule(module, release)
}

