package vf.optidepy.database.access.single.deployment.config

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.NonDeprecatedView
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.deployment.ProjectDeploymentConfigDbFactory
import vf.optidepy.database.storable.deployment.DeploymentConfigDbModel
import vf.optidepy.database.storable.project.ProjectDbModel
import vf.optidepy.model.combined.deployment.ProjectDeploymentConfig

/**
  * Used for accessing individual project deployment configs
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbProjectDeploymentConfig 
	extends SingleRowModelAccess[ProjectDeploymentConfig] with NonDeprecatedView[ProjectDeploymentConfig] 
		with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * A database model (factory) used for interacting with linked configs
	  */
	protected def model = DeploymentConfigDbModel
	
	/**
	  * A database model (factory) used for interacting with the linked project
	  */
	protected def projectModel = ProjectDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = ProjectDeploymentConfigDbFactory
	
	
	// OTHER	--------------------
	
	/**
	  * @param id Database id of the targeted project deployment config
	  * @return An access point to that project deployment config
	  */
	def apply(id: Int) = DbSingleProjectDeploymentConfig(id)
	
	/**
	  * 
		@param additionalCondition Filter condition to apply in addition to this root view's condition. Should yield
	  *  unique project deployment configs.
	  * @return An access point to the project deployment config that satisfies the specified condition
	  */
	protected def filterDistinct(additionalCondition: Condition) = 
		UniqueProjectDeploymentConfigAccess(mergeCondition(additionalCondition))
}

