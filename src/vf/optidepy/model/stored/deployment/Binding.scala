package vf.optidepy.model.stored.deployment

import utopia.flow.generic.model.template.ModelLike.AnyModel
import utopia.vault.model.template.{FromIdFactory, StoredFromModelFactory, StoredModelConvertible}
import vf.optidepy.database.access.single.deployment.binding.DbSingleBinding
import vf.optidepy.model.factory.deployment.BindingFactoryWrapper
import vf.optidepy.model.partial.deployment.BindingData

object Binding extends StoredFromModelFactory[BindingData, Binding]
{
	// IMPLEMENTED	--------------------
	
	override def dataFactory = BindingData
	
	override protected def complete(model: AnyModel, data: BindingData) = 
		model("id").tryInt.map { apply(_, data) }
}

/**
  * Represents a binding that has already been stored in the database
  * @param id id of this binding in the database
  * @param data Wrapped binding data
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class Binding(id: Int, data: BindingData) 
	extends StoredModelConvertible[BindingData] with FromIdFactory[Int, Binding] 
		with BindingFactoryWrapper[BindingData, Binding]
{
	// COMPUTED	--------------------
	
	/**
	  * An access point to this binding in the database
	  */
	def access = DbSingleBinding(id)
	
	
	// IMPLEMENTED	--------------------
	
	override protected def wrappedFactory = data
	
	override def withId(id: Int) = copy(id = id)
	
	override protected def wrap(data: BindingData) = copy(data = data)
}
