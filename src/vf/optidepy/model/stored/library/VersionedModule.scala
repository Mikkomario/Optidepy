package vf.optidepy.model.stored.library

import utopia.flow.generic.model.template.ModelLike.AnyModel
import utopia.vault.model.template.{FromIdFactory, StoredFromModelFactory, StoredModelConvertible}
import vf.optidepy.database.access.single.library.module.DbSingleVersionedModule
import vf.optidepy.model.factory.library.VersionedModuleFactoryWrapper
import vf.optidepy.model.partial.library.VersionedModuleData

object VersionedModule extends StoredFromModelFactory[VersionedModuleData, VersionedModule]
{
	// IMPLEMENTED	--------------------
	
	override def dataFactory = VersionedModuleData
	
	override protected def complete(model: AnyModel, data: VersionedModuleData) = 
		model("id").tryInt.map { apply(_, data) }
}

/**
  * Represents a versioned module that has already been stored in the database
  * @param id id of this versioned module in the database
  * @param data Wrapped versioned module data
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class VersionedModule(id: Int, data: VersionedModuleData) 
	extends StoredModelConvertible[VersionedModuleData] with FromIdFactory[Int, VersionedModule] 
		with VersionedModuleFactoryWrapper[VersionedModuleData, VersionedModule]
{
	// COMPUTED	--------------------
	
	/**
	  * An access point to this versioned module in the database
	  */
	def access = DbSingleVersionedModule(id)
	
	
	// IMPLEMENTED	--------------------
	
	override protected def wrappedFactory = data
	
	override def withId(id: Int) = copy(id = id)
	
	override protected def wrap(data: VersionedModuleData) = copy(data = data)
}

