package vf.optidepy.database.access.single.deployment.branch

import utopia.flow.util.Version
import utopia.vault.database.Connection
import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.view.{NullDeprecatableView, ViewFactory}
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.deployment.PossiblyDeployedBranchDbFactory
import vf.optidepy.database.storable.deployment.DeploymentDbModel
import vf.optidepy.model.combined.deployment.PossiblyDeployedBranch

object UniquePossiblyDeployedBranchAccess extends ViewFactory[UniquePossiblyDeployedBranchAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): UniquePossiblyDeployedBranchAccess = 
		_UniquePossiblyDeployedBranchAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _UniquePossiblyDeployedBranchAccess(override val accessCondition: Option[Condition]) 
		extends UniquePossiblyDeployedBranchAccess
}

/**
  * A common trait for access points that return distinct possibly deployed branches
  * @author Mikko Hilpinen
  * @since 10.08.2024, v1.2
  */
trait UniquePossiblyDeployedBranchAccess 
	extends UniqueBranchAccessLike[PossiblyDeployedBranch, UniquePossiblyDeployedBranchAccess] 
		with SingleRowModelAccess[PossiblyDeployedBranch] 
		with NullDeprecatableView[UniquePossiblyDeployedBranchAccess]
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
	def deploymentDeploymentIndex(implicit connection: Connection) = 
		pullColumn(deploymentModel.deploymentIndex.column).int
	
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
	
	override def factory = PossiblyDeployedBranchDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): UniquePossiblyDeployedBranchAccess = 
		UniquePossiblyDeployedBranchAccess(condition)
}

