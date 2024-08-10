package vf.optidepy.model.combined.library

import utopia.flow.view.template.Extender
import utopia.vault.model.template.HasId
import vf.optidepy.model.factory.library.VersionedModuleFactoryWrapper
import vf.optidepy.model.partial.library.VersionedModuleData
import vf.optidepy.model.stored.library.VersionedModule
import vf.optidepy.model.stored.project.Project

/**
  * Combines project and module information
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class VersionedProjectModule(module: VersionedModule, project: Project) 
	extends Extender[VersionedModuleData] with HasId[Int] 
		with VersionedModuleFactoryWrapper[VersionedModule, VersionedProjectModule]
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

