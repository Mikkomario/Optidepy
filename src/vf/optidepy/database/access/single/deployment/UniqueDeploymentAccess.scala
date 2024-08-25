package vf.optidepy.database.access.single.deployment

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.model.immutable.Value
import utopia.flow.util.Version
import utopia.vault.database.Connection
import utopia.vault.nosql.access.single.model.SingleChronoRowModelAccess
import utopia.vault.nosql.access.template.model.DistinctModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.{NullDeprecatableView, ViewFactory}
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.deployment.DeploymentDbFactory
import vf.optidepy.database.storable.deployment.DeploymentDbModel
import vf.optidepy.model.stored.deployment.Deployment

import java.time.Instant

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
  * @since 24.08.2024, v1.2
  */
trait UniqueDeploymentAccess 
	extends SingleChronoRowModelAccess[Deployment, UniqueDeploymentAccess] 
		with DistinctModelAccess[Deployment, Option[Deployment], Value] 
		with NullDeprecatableView[UniqueDeploymentAccess] with Indexed
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
	  * Deployed project version. None if versioning is not being used. 
	  * None if no deployment (or value) was found.
	  */
	def version(implicit connection: Connection) = Some(Version(pullColumn(model.version.column).getString))
	
	/**
	  * Time when this deployment was made. 
	  * None if no deployment (or value) was found.
	  */
	def created(implicit connection: Connection) = pullColumn(model.created.column).instant
	
	/**
	  * 
		Timestamp after which this was no longer the latest deployment. None while this is the latest deployment. 
	  * None if no deployment (or value) was found.
	  */
	def latestUntil(implicit connection: Connection) = pullColumn(model.latestUntil.column).instant
	
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
	
	
	// OTHER	--------------------
	
	/**
	  * Updates the latest untils of the targeted deployments
	  * @param newLatestUntil A new latest until to assign
	  * @return Whether any deployment was affected
	  */
	def latestUntil_=(newLatestUntil: Instant)(implicit connection: Connection) = 
		putColumn(model.latestUntil.column, newLatestUntil)
}

