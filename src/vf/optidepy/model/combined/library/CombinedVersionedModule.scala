package vf.optidepy.model.combined.library

import utopia.flow.view.template.Extender
import utopia.vault.model.template.HasId
import vf.optidepy.model.factory.library.VersionedModuleFactoryWrapper
import vf.optidepy.model.partial.library.VersionedModuleData
import vf.optidepy.model.stored.library.VersionedModule

/**
  * Common trait for combinations that add additional data to versioned modules
  * @tparam Repr Type of the implementing class
  * @author Mikko Hilpinen
  * @since 24.08.2024, v1.2
  */
trait CombinedVersionedModule[+Repr] 
	extends Extender[VersionedModuleData] with HasId[Int] 
		with VersionedModuleFactoryWrapper[VersionedModule, Repr]
{
	// ABSTRACT	--------------------
	
	/**
	  * Wrapped versioned module
	  */
	def versionedModule: VersionedModule
	
	
	// IMPLEMENTED	--------------------
	
	/**
	  * Id of this versioned module in the database
	  */
	override def id = versionedModule.id
	
	override def wrapped = versionedModule.data
	
	override protected def wrappedFactory = versionedModule
}

