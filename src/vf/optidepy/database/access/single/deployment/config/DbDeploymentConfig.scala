package vf.optidepy.database.access.single.deployment.config

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.NonDeprecatedView
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.deployment.DeploymentConfigDbFactory
import vf.optidepy.database.storable.deployment.DeploymentConfigDbModel
import vf.optidepy.model.stored.deployment.DeploymentConfig

/**
  * Used for accessing individual deployment configs
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbDeploymentConfig 
	extends SingleRowModelAccess[DeploymentConfig] with NonDeprecatedView[DeploymentConfig] with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	protected def model = DeploymentConfigDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = DeploymentConfigDbFactory
	
	
	// OTHER	--------------------
	
	/**
	  * @param id Database id of the targeted deployment config
	  * @return An access point to that deployment config
	  */
	def apply(id: Int) = DbSingleDeploymentConfig(id)
	
	/**
	  * 
		@param additionalCondition Filter condition to apply in addition to this root view's condition. Should yield
	  *  unique deployment configs.
	  * @return An access point to the deployment config that satisfies the specified condition
	  */
	protected def filterDistinct(additionalCondition: Condition) = 
		UniqueDeploymentConfigAccess(mergeCondition(additionalCondition))
}

