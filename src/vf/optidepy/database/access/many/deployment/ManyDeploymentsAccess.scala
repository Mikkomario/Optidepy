package vf.optidepy.database.access.many.deployment

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.util.Version
import utopia.vault.database.Connection
import utopia.vault.nosql.access.many.model.ManyRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.{ChronoRowFactoryView, ViewFactory}
import utopia.vault.sql.Condition
import utopia.vault.sql.OrderDirection.{Ascending, Descending}
import vf.optidepy.database.access.single.deployment.LatestOrEarliestDeploymentAccess
import vf.optidepy.database.factory.deployment.DeploymentDbFactory
import vf.optidepy.database.storable.deployment.DeploymentDbModel
import vf.optidepy.model.stored.deployment.Deployment

object ManyDeploymentsAccess extends ViewFactory[ManyDeploymentsAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): ManyDeploymentsAccess = _ManyDeploymentsAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _ManyDeploymentsAccess(override val accessCondition: Option[Condition]) 
		extends ManyDeploymentsAccess
}

/**
  * A common trait for access points which target multiple deployments at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait ManyDeploymentsAccess 
	extends ManyRowModelAccess[Deployment] with ChronoRowFactoryView[Deployment, ManyDeploymentsAccess] 
		with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * branch ids of the accessible deployments
	  */
	def branchIds(implicit connection: Connection) = pullColumn(model.branchId.column).map { v => v.getInt }
	/**
	  * indices of the accessible deployments
	  */
	def indices(implicit connection: Connection) = pullColumn(model.deploymentIndex.column).map { v => v.getInt }
	/**
	  * creation times of the accessible deployments
	  */
	def creationTimes(implicit connection: Connection) = 
		pullColumn(model.created.column).map { v => v.getInstant }
	/**
	  * versions of the accessible deployments
	  */
	def versions(implicit connection: Connection) = 
		pullColumn(model.version.column).flatMap { _.string }.map { v => Some(Version(v)) }
	/**
	  * Unique ids of the accessible deployments
	  */
	def ids(implicit connection: Connection) = pullColumn(index).map { v => v.getInt }
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	protected def model = DeploymentDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = DeploymentDbFactory
	override protected def self = this
	
	override def latest = LatestOrEarliestDeploymentAccess(Descending, accessCondition)
	override def earliest = LatestOrEarliestDeploymentAccess(Ascending, accessCondition)
	
	override def apply(condition: Condition): ManyDeploymentsAccess = ManyDeploymentsAccess(condition)
	
	
	// OTHER	--------------------
	
	/**
	  * @param branchId branch id to target
	  * @return Copy of this access point that only includes deployments with the specified branch id
	  */
	def ofBranch(branchId: Int) = filter(model.branchId.column <=> branchId)
	/**
	  * @param branchIds Targeted branch ids
	  * @return Copy of this access point that only includes deployments where branch id is within the specified
	  *  value set
	  */
	def ofBranches(branchIds: Iterable[Int]) = filter(model.branchId.column.in(branchIds))
	
	/**
	  * @param version version to target
	  * @return Copy of this access point that only includes deployments with the specified version
	  */
	def ofVersion(version: Version) = filter(model.version.column <=> version.toString)
	/**
	  * @param versions Targeted versions
	  * @return Copy of this access point that only includes deployments where version is within the
	  *  specified value set
	  */
	def ofVersions(versions: Iterable[Version]) = 
		filter(model.version.column.in(versions.map { version => version.toString }))
}

