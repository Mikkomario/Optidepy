package vf.optidepy.model.stored.dependency

import utopia.flow.generic.model.template.ModelLike.AnyModel
import utopia.vault.model.template.{FromIdFactory, StoredFromModelFactory, StoredModelConvertible}
import vf.optidepy.database.access.single.dependency.DbSingleDependency
import vf.optidepy.model.factory.dependency.DependencyFactoryWrapper
import vf.optidepy.model.partial.dependency.DependencyData

object Dependency extends StoredFromModelFactory[DependencyData, Dependency]
{
	// IMPLEMENTED	--------------------
	
	override def dataFactory = DependencyData
	
	override protected def complete(model: AnyModel, data: DependencyData) = 
		model("id").tryInt.map { apply(_, data) }
}

/**
  * Represents a dependency that has already been stored in the database
  * @param id id of this dependency in the database
  * @param data Wrapped dependency data
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class Dependency(id: Int, data: DependencyData) 
	extends StoredModelConvertible[DependencyData] with FromIdFactory[Int, Dependency] 
		with DependencyFactoryWrapper[DependencyData, Dependency]
{
	// COMPUTED	--------------------
	
	/**
	  * An access point to this dependency in the database
	  */
	def access = DbSingleDependency(id)
	
	
	// IMPLEMENTED	--------------------
	
	override protected def wrappedFactory = data
	
	override def withId(id: Int) = copy(id = id)
	
	override protected def wrap(data: DependencyData) = copy(data = data)
}

