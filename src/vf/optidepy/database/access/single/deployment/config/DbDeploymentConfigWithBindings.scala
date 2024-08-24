package vf.optidepy.database.access.single.deployment.config

import utopia.vault.nosql.access.single.model.SingleModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.NonDeprecatedView
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.deployment.DeploymentConfigWithBindingsDbFactory
import vf.optidepy.database.storable.deployment.{BindingDbModel, DeploymentConfigDbModel}
import vf.optidepy.model.combined.deployment.DeploymentConfigWithBindings

/**
  * Used for accessing individual detailed deployment configs
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbDeploymentConfigWithBindings
	extends SingleModelAccess[DeploymentConfigWithBindings] with NonDeprecatedView[DeploymentConfigWithBindings]
		with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * A database model (factory) used for interacting with linked configs
	  */
	protected def model = DeploymentConfigDbModel
	/**
	  * A database model (factory) used for interacting with the linked binding
	  */
	protected def bindingModel = BindingDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = DeploymentConfigWithBindingsDbFactory
	
	
	// OTHER	--------------------
	
	/**
	  * @param id Database id of the targeted detailed deployment config
	  * @return An access point to that detailed deployment config
	  */
	def apply(id: Int) = DbSingleDeploymentConfigWithBindings(id)
	
	/**
	  * @param additionalCondition Filter condition to apply in addition to this root view's condition. Should yield
	  *  unique detailed deployment configs.
	  * @return An access point to the detailed deployment config that satisfies the specified condition
	  */
	protected def filterDistinct(additionalCondition: Condition) = 
		UniqueDeploymentConfigWithBindingsAccess(mergeCondition(additionalCondition))
}

