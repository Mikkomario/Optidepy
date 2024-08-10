package vf.optidepy.database.access.single.deployment

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.UnconditionalView
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.deployment.DeploymentDbFactory
import vf.optidepy.database.storable.deployment.DeploymentDbModel
import vf.optidepy.model.stored.deployment.Deployment

/**
  * Used for accessing individual deployments
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbDeployment extends SingleRowModelAccess[Deployment] with UnconditionalView with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	protected def model = DeploymentDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = DeploymentDbFactory
	
	
	// OTHER	--------------------
	
	/**
	  * @param id Database id of the targeted deployment
	  * @return An access point to that deployment
	  */
	def apply(id: Int) = DbSingleDeployment(id)
	
	/**
	  * @param condition Filter condition to apply in addition to this root view's condition. Should yield
	  *  unique deployments.
	  * @return An access point to the deployment that satisfies the specified condition
	  */
	protected def distinct(condition: Condition) = UniqueDeploymentAccess(condition)
}

