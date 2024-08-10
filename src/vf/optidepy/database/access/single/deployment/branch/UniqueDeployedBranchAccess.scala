package vf.optidepy.database.access.single.deployment.branch

import utopia.flow.util.Version
import utopia.vault.database.Connection
import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.view.{NullDeprecatableView, ViewFactory}
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.deployment.DeployedBranchDbFactory
import vf.optidepy.database.storable.deployment.DeploymentDbModel
import vf.optidepy.model.combined.deployment.DeployedBranch

object UniqueDeployedBranchAccess extends ViewFactory[UniqueDeployedBranchAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): UniqueDeployedBranchAccess = 
		_UniqueDeployedBranchAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _UniqueDeployedBranchAccess(override val accessCondition: Option[Condition]) 
		extends UniqueDeployedBranchAccess
}

/**
  * A common trait for access points that return distinct deployed branchs
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait UniqueDeployedBranchAccess 
	extends UniqueBranchAccessLike[DeployedBranch, UniqueDeployedBranchAccess] 
		with SingleRowModelAccess[DeployedBranch] with NullDeprecatableView[UniqueDeployedBranchAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * Id of the branch that was deployed. 
	  * None if no deployment (or value) was found.
	  */
	def deploymentBranchId(implicit connection: Connection) = pullColumn(deploymentModel.branchId.column).int
	
	/**
	  * Ordered index of this deployment. Relative to other deployments targeting the same branch. 
	  * None if no deployment (or value) was found.
	  */
	def deploymentIndex(implicit connection: Connection) = pullColumn(deploymentModel.deploymentIndex.column).int
	
	/**
	  * Time when this deployment was made. 
	  * None if no deployment (or value) was found.
	  */
	def deploymentCreated(implicit connection: Connection) = pullColumn(deploymentModel
		.created.column).instant
	
	/**
	  * Deployed project version. None if versioning is not being used. 
	  * None if no deployment (or value) was found.
	  */
	def deploymentVersion(implicit connection: Connection) = 
		Some(Version(pullColumn(deploymentModel.version.column).getString))
	
	/**
	  * A database model (factory) used for interacting with the linked deployment
	  */
	protected def deploymentModel = DeploymentDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = DeployedBranchDbFactory
	
	override protected def self = this
	
	override
		 def apply(condition: Condition): UniqueDeployedBranchAccess = UniqueDeployedBranchAccess(condition)
}

