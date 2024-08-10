package vf.optidepy.model.stored.deployment

import utopia.flow.generic.model.template.ModelLike.AnyModel
import utopia.vault.model.template.{FromIdFactory, StoredFromModelFactory, StoredModelConvertible}
import vf.optidepy.database.access.single.deployment.config.DbSingleDeploymentConfig
import vf.optidepy.model.factory.deployment.DeploymentConfigFactoryWrapper
import vf.optidepy.model.partial.deployment.DeploymentConfigData

object DeploymentConfig extends StoredFromModelFactory[DeploymentConfigData, DeploymentConfig]
{
	// IMPLEMENTED	--------------------
	
	override def dataFactory = DeploymentConfigData
	
	override protected def complete(model: AnyModel, data: DeploymentConfigData) = 
		model("id").tryInt.map { apply(_, data) }
}

/**
  * Represents a deployment config that has already been stored in the database
  * @param id id of this deployment config in the database
  * @param data Wrapped deployment config data
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DeploymentConfig(id: Int, data: DeploymentConfigData) 
	extends StoredModelConvertible[DeploymentConfigData] with FromIdFactory[Int, DeploymentConfig] 
		with DeploymentConfigFactoryWrapper[DeploymentConfigData, DeploymentConfig]
{
	// COMPUTED	--------------------
	
	/**
	  * An access point to this deployment config in the database
	  */
	def access = DbSingleDeploymentConfig(id)
	
	
	// IMPLEMENTED	--------------------
	
	override protected def wrappedFactory = data
	
	override def withId(id: Int) = copy(id = id)
	
	override protected def wrap(data: DeploymentConfigData) = copy(data = data)
}

