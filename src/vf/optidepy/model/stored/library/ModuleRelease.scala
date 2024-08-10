package vf.optidepy.model.stored.library

import utopia.flow.generic.model.template.ModelLike.AnyModel
import utopia.vault.model.template.{FromIdFactory, StoredFromModelFactory, StoredModelConvertible}
import vf.optidepy.database.access.single.library.module.release.DbSingleModuleRelease
import vf.optidepy.model.factory.library.ModuleReleaseFactoryWrapper
import vf.optidepy.model.partial.library.ModuleReleaseData

object ModuleRelease extends StoredFromModelFactory[ModuleReleaseData, ModuleRelease]
{
	// IMPLEMENTED	--------------------
	
	override def dataFactory = ModuleReleaseData
	
	override protected def complete(model: AnyModel, data: ModuleReleaseData) = 
		model("id").tryInt.map { apply(_, data) }
}

/**
  * Represents a module release that has already been stored in the database
  * @param id id of this module release in the database
  * @param data Wrapped module release data
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class ModuleRelease(id: Int, data: ModuleReleaseData) 
	extends StoredModelConvertible[ModuleReleaseData] with FromIdFactory[Int, ModuleRelease] 
		with ModuleReleaseFactoryWrapper[ModuleReleaseData, ModuleRelease]
{
	// COMPUTED	--------------------
	
	/**
	  * An access point to this module release in the database
	  */
	def access = DbSingleModuleRelease(id)
	
	
	// IMPLEMENTED	--------------------
	
	override protected def wrappedFactory = data
	
	override def withId(id: Int) = copy(id = id)
	
	override protected def wrap(data: ModuleReleaseData) = copy(data = data)
}

