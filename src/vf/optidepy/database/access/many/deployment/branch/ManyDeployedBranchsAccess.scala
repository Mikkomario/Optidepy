package vf.optidepy.database.access.many.deployment.branch

import utopia.flow.util.Version
import utopia.vault.database.Connection
import utopia.vault.nosql.access.many.model.ManyRowModelAccess
import utopia.vault.nosql.view.ViewFactory
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.deployment.DeployedBranchDbFactory
import vf.optidepy.database.storable.deployment.DeploymentDbModel
import vf.optidepy.model.combined.deployment.DeployedBranch

object ManyDeployedBranchsAccess extends ViewFactory[ManyDeployedBranchsAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): ManyDeployedBranchsAccess = 
		_ManyDeployedBranchsAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _ManyDeployedBranchsAccess(override val accessCondition: Option[Condition]) 
		extends ManyDeployedBranchsAccess
}

/**
  * A common trait for access points that return multiple deployed branchs at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024
  */
trait ManyDeployedBranchsAccess 
	extends ManyBranchesAccessLike[DeployedBranch, ManyDeployedBranchsAccess] 
		with ManyRowModelAccess[DeployedBranch]
{
	// COMPUTED	--------------------
	
	/**
	  * branch ids of the accessible deployments
	  */
	def deploymentBranchIds(implicit connection: Connection) = 
		pullColumn(deploymentModel.branchId.column).map { v => v.getInt }
	
	/**
	  * indices of the accessible deployments
	  */
	def deploymentIndices(implicit connection: Connection) = 
		pullColumn(deploymentModel.deploymentIndex.column).map { v => v.getInt }
	
	/**
	  * creation times of the accessible deployments
	  */
	def deploymentCreationTimes(implicit connection: Connection) = 
		pullColumn(deploymentModel.created.column).map { v => v.getInstant }
	
	/**
	  * versions of the accessible deployments
	  */
	def deploymentVersions(implicit connection: Connection) = 
		pullColumn(deploymentModel.version.column).flatMap { _.string }.map { v => Some(Version(v)) }
	
	/**
	  * Model (factory) used for interacting the deployments associated with this deployed branch
	  */
	protected def deploymentModel = DeploymentDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = DeployedBranchDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): ManyDeployedBranchsAccess = ManyDeployedBranchsAccess(condition)
}

