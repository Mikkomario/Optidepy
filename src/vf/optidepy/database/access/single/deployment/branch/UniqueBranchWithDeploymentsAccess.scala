package vf.optidepy.database.access.single.deployment.branch

import utopia.vault.nosql.view.{NullDeprecatableView, ViewFactory}
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.deployment.BranchWithDeploymentsDbFactory
import vf.optidepy.database.storable.deployment.DeploymentDbModel
import vf.optidepy.model.combined.deployment.BranchWithDeployments

object UniqueBranchWithDeploymentsAccess extends ViewFactory[UniqueBranchWithDeploymentsAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): UniqueBranchWithDeploymentsAccess = 
		_UniqueBranchWithDeploymentsAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _UniqueBranchWithDeploymentsAccess(override val accessCondition: Option[Condition]) 
		extends UniqueBranchWithDeploymentsAccess
}

/**
  * A common trait for access points that return distinct branches with deployments
  * @author Mikko Hilpinen
  * @since 10.08.2024, v1.2
  */
trait UniqueBranchWithDeploymentsAccess 
	extends UniqueBranchAccessLike[BranchWithDeployments, UniqueBranchWithDeploymentsAccess] 
		with NullDeprecatableView[UniqueBranchWithDeploymentsAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * A database model (factory) used for interacting with the linked deployments
	  */
	protected def deploymentModel = DeploymentDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = BranchWithDeploymentsDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): UniqueBranchWithDeploymentsAccess = 
		UniqueBranchWithDeploymentsAccess(condition)
}

