package vf.optidepy.database.access.single.deployment

import utopia.flow.generic.model.immutable.Value
import utopia.flow.util.Version
import utopia.vault.database.Connection
import utopia.vault.nosql.access.single.model.SingleChronoRowModelAccess
import utopia.vault.nosql.access.template.model.DistinctModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.{FilterableView, ViewFactory}
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.deployment.DeploymentDbFactory
import vf.optidepy.database.storable.deployment.DeploymentDbModel
import vf.optidepy.model.stored.deployment.Deployment

object UniqueDeploymentAccess extends ViewFactory[UniqueDeploymentAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override
		 def apply(condition: Condition): UniqueDeploymentAccess = _UniqueDeploymentAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _UniqueDeploymentAccess(override val accessCondition: Option[Condition]) 
		extends UniqueDeploymentAccess
}

/**
  * A common trait for access points that return individual and distinct deployments.
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait UniqueDeploymentAccess 
	extends SingleChronoRowModelAccess[Deployment, UniqueDeploymentAccess] 
		with DistinctModelAccess[Deployment, Option[Deployment], Value] 
		with FilterableView[UniqueDeploymentAccess] with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * Id of the branch that was deployed. 
	  * None if no deployment (or value) was found.
	  */
	def branchId(implicit connection: Connection) = pullColumn(model.branchId.column).int
	
	/**
	  * Ordered index of this deployment. Relative to other deployments targeting the same branch. 
	  * None if no deployment (or value) was found.
	  */
	def deploymentIndex(implicit connection: Connection) = pullColumn(model.deploymentIndex.column).int
	
	/**
	  * Time when this deployment was made. 
	  * None if no deployment (or value) was found.
	  */
	def created(implicit connection: Connection) = pullColumn(model.created.column).instant
	
	/**
	  * Deployed project version. None if versioning is not being used. 
	  * None if no deployment (or value) was found.
	  */
	def version(implicit connection: Connection) = Some(Version(pullColumn(model.version.column).getString))
	
	/**
	  * Unique id of the accessible deployment. None if no deployment was accessible.
	  */
	def id(implicit connection: Connection) = pullColumn(index).int
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	protected def model = DeploymentDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = DeploymentDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): UniqueDeploymentAccess = UniqueDeploymentAccess(condition)
}

