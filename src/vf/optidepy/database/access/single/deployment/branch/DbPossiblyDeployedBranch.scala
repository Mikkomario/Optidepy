package vf.optidepy.database.access.single.deployment.branch

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.NonDeprecatedView
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.deployment.PossiblyDeployedBranchDbFactory
import vf.optidepy.database.storable.deployment.{BranchDbModel, DeploymentDbModel}
import vf.optidepy.model.combined.deployment.PossiblyDeployedBranch

/**
  * Used for accessing individual possibly deployed branches
  * @author Mikko Hilpinen
  * @since 10.08.2024, v1.2
  */
object DbPossiblyDeployedBranch 
	extends SingleRowModelAccess[PossiblyDeployedBranch] with NonDeprecatedView[PossiblyDeployedBranch] 
		with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * A database model (factory) used for interacting with the linked deployment
	  */
	protected def deploymentModel = DeploymentDbModel
	
	/**
	  * A database model (factory) used for interacting with linked branches
	  */
	private def model = BranchDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = PossiblyDeployedBranchDbFactory
	
	
	// OTHER	--------------------
	
	/**
	  * @param id Database id of the targeted possibly deployed branch
	  * @return An access point to that possibly deployed branch
	  */
	def apply(id: Int) = DbSinglePossiblyDeployedBranch(id)
	
	/**
	  * 
		@param additionalCondition Filter condition to apply in addition to this root view's condition. Should yield
	  *  unique possibly deployed branches.
	  * @return An access point to the possibly deployed branch that satisfies the specified condition
	  */
	protected def filterDistinct(additionalCondition: Condition) = 
		UniquePossiblyDeployedBranchAccess(mergeCondition(additionalCondition))
}

