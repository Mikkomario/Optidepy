package vf.optidepy.database.access.many.deployment.branch

import utopia.flow.util.Version
import utopia.vault.database.Connection
import utopia.vault.nosql.access.many.model.ManyRowModelAccess
import utopia.vault.nosql.view.ViewFactory
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.deployment.PossiblyDeployedBranchDbFactory
import vf.optidepy.database.storable.deployment.DeploymentDbModel
import vf.optidepy.model.combined.deployment.PossiblyDeployedBranch

object ManyPossiblyDeployedBranchesAccess extends ViewFactory[ManyPossiblyDeployedBranchesAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): ManyPossiblyDeployedBranchesAccess = 
		_ManyPossiblyDeployedBranchesAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _ManyPossiblyDeployedBranchesAccess(override val accessCondition: Option[Condition]) 
		extends ManyPossiblyDeployedBranchesAccess
}

/**
  * A common trait for access points that return multiple possibly deployed branches at a time
  * @author Mikko Hilpinen
  * @since 10.08.2024
  */
trait ManyPossiblyDeployedBranchesAccess 
	extends ManyBranchesAccessLike[PossiblyDeployedBranch, ManyPossiblyDeployedBranchesAccess] 
		with ManyRowModelAccess[PossiblyDeployedBranch]
{
	// COMPUTED	--------------------
	
	/**
	  * branch ids of the accessible deployments
	  */
	def deploymentBranchIds(implicit connection: Connection) = 
		pullColumn(deploymentModel.branchId.column).map { v => v.getInt }
	
	/**
	  * deployment indices of the accessible deployments
	  */
	def deploymentDeploymentIndices(implicit connection: Connection) = 
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
	  * Model (factory) used for interacting the deployments associated with this possibly deployed branch
	  */
	protected def deploymentModel = DeploymentDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = PossiblyDeployedBranchDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): ManyPossiblyDeployedBranchesAccess = 
		ManyPossiblyDeployedBranchesAccess(condition)
}

