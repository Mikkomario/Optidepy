package vf.optidepy.database.access.single.deployment.branch

import utopia.vault.nosql.access.single.model.SingleModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.NonDeprecatedView
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.deployment.BranchWithDeploymentsDbFactory
import vf.optidepy.database.storable.deployment.{BranchDbModel, DeploymentDbModel}
import vf.optidepy.model.combined.deployment.BranchWithDeployments

/**
  * Used for accessing individual branches with deployments
  * @author Mikko Hilpinen
  * @since 10.08.2024, v1.2
  */
object DbBranchWithDeployments 
	extends SingleModelAccess[BranchWithDeployments] with NonDeprecatedView[BranchWithDeployments] 
		with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * A database model (factory) used for interacting with the linked deployments
	  */
	protected def deploymentModel = DeploymentDbModel
	
	/**
	  * A database model (factory) used for interacting with linked branches
	  */
	private def model = BranchDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = BranchWithDeploymentsDbFactory
	
	
	// OTHER	--------------------
	
	/**
	  * @param id Database id of the targeted branch with deployments
	  * @return An access point to that branch with deployments
	  */
	def apply(id: Int) = DbSingleBranchWithDeployments(id)
	
	/**
	  * 
		@param additionalCondition Filter condition to apply in addition to this root view's condition. Should yield unique branches 
	  * with deployments.
	  * @return An access point to the branch with deployments that satisfies the specified condition
	  */
	protected def filterDistinct(additionalCondition: Condition) = 
		UniqueBranchWithDeploymentsAccess(mergeCondition(additionalCondition))
}

