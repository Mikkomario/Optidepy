package vf.optidepy.database.access.single.deployment.branch

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.NonDeprecatedView
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.deployment.DeployedBranchDbFactory
import vf.optidepy.database.storable.deployment.{BranchDbModel, DeploymentDbModel}
import vf.optidepy.model.combined.deployment.DeployedBranch

/**
  * Used for accessing individual deployed branchs
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbDeployedBranch 
	extends SingleRowModelAccess[DeployedBranch] with NonDeprecatedView[DeployedBranch] with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * A database model (factory) used for interacting with linked branches
	  */
	protected def model = BranchDbModel
	
	/**
	  * A database model (factory) used for interacting with the linked deployment
	  */
	protected def deploymentModel = DeploymentDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = DeployedBranchDbFactory
	
	
	// OTHER	--------------------
	
	/**
	  * @param id Database id of the targeted deployed branch
	  * @return An access point to that deployed branch
	  */
	def apply(id: Int) = DbSingleDeployedBranch(id)
	
	/**
	  * 
		@param additionalCondition Filter condition to apply in addition to this root view's condition. Should yield
	  *  unique deployed branchs.
	  * @return An access point to the deployed branch that satisfies the specified condition
	  */
	protected def filterDistinct(additionalCondition: Condition) = 
		UniqueDeployedBranchAccess(mergeCondition(additionalCondition))
}

