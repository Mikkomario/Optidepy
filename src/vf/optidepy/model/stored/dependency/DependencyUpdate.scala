package vf.optidepy.model.stored.dependency

import utopia.flow.generic.model.template.ModelLike.AnyModel
import utopia.vault.model.template.{FromIdFactory, StoredFromModelFactory, StoredModelConvertible}
import vf.optidepy.database.access.single.dependency.update.DbSingleDependencyUpdate
import vf.optidepy.model.factory.dependency.DependencyUpdateFactoryWrapper
import vf.optidepy.model.partial.dependency.DependencyUpdateData

object DependencyUpdate extends StoredFromModelFactory[DependencyUpdateData, DependencyUpdate]
{
	// IMPLEMENTED	--------------------
	
	override def dataFactory = DependencyUpdateData
	
	override protected def complete(model: AnyModel, data: DependencyUpdateData) = 
		model("id").tryInt.map { apply(_, data) }
}

/**
  * Represents a dependency update that has already been stored in the database
  * @param id id of this dependency update in the database
  * @param data Wrapped dependency update data
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DependencyUpdate(id: Int, data: DependencyUpdateData) 
	extends StoredModelConvertible[DependencyUpdateData] with FromIdFactory[Int, DependencyUpdate] 
		with DependencyUpdateFactoryWrapper[DependencyUpdateData, DependencyUpdate]
{
	// COMPUTED	--------------------
	
	/**
	  * An access point to this dependency update in the database
	  */
	def access = DbSingleDependencyUpdate(id)
	
	
	// IMPLEMENTED	--------------------
	
	override protected def wrappedFactory = data
	
	override def withId(id: Int) = copy(id = id)
	
	override protected def wrap(data: DependencyUpdateData) = copy(data = data)
}

