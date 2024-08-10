package vf.optidepy.model.combined.library

import utopia.flow.view.template.Extender
import utopia.vault.model.template.HasId
import vf.optidepy.model.factory.library.VersionedModuleFactoryWrapper
import vf.optidepy.model.partial.library.VersionedModuleData
import vf.optidepy.model.stored.library.{ModuleRelease, VersionedModule}

/**
  * Includes a single release's information in a module
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class ReleasedModule(module: VersionedModule, release: ModuleRelease) 
	extends Extender[VersionedModuleData] with HasId[Int] 
		with VersionedModuleFactoryWrapper[VersionedModule, ReleasedModule]
{
	// COMPUTED	--------------------
	
	/**
	  * Id of this module in the database
	  */
	def id = module.id
	
	
	// IMPLEMENTED	--------------------
	
	override def wrapped = module.data
	
	override protected def wrappedFactory = module
	
	override protected def wrap(factory: VersionedModule) = copy(module = factory)
}

