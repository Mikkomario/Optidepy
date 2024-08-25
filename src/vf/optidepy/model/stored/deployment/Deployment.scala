package vf.optidepy.model.stored.deployment

import utopia.flow.generic.model.template.ModelLike.AnyModel
import utopia.vault.model.template.{FromIdFactory, StoredFromModelFactory, StoredModelConvertible}
import vf.optidepy.database.access.single.deployment.DbSingleDeployment
import vf.optidepy.model.factory.deployment.DeploymentFactoryWrapper
import vf.optidepy.model.partial.deployment.DeploymentData

object Deployment extends StoredFromModelFactory[DeploymentData, Deployment]
{
	// IMPLEMENTED	--------------------
	
	override def dataFactory = DeploymentData
	
	override protected def complete(model: AnyModel, data: DeploymentData) = 
		model("id").tryInt.map { apply(_, data) }
}

/**
  * Represents a deployment that has already been stored in the database
  * @param id id of this deployment in the database
  * @param data Wrapped deployment data
  * @author Mikko Hilpinen
  * @since 24.08.2024, v1.2
  */
case class Deployment(id: Int, data: DeploymentData) 
	extends StoredModelConvertible[DeploymentData] with FromIdFactory[Int, Deployment] 
		with DeploymentFactoryWrapper[DeploymentData, Deployment]
{
	// COMPUTED	--------------------
	
	/**
	  * An access point to this deployment in the database
	  */
	def access = DbSingleDeployment(id)
	
	
	// IMPLEMENTED	--------------------
	
	override protected def wrappedFactory = data
	
	override def withId(id: Int) = copy(id = id)
	
	override protected def wrap(data: DeploymentData) = copy(data = data)
}

