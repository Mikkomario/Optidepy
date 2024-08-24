package vf.optidepy.database.access.single.deployment

import utopia.vault.nosql.access.single.model.distinct.LatestOrEarliestModelAccess
import utopia.vault.sql.{Condition, OrderDirection}
import vf.optidepy.model.stored.deployment.Deployment

object LatestOrEarliestDeploymentAccess
{
	// OTHER    --------------------------
	
	/**
	 * @param direction Applied ordering direction
	 * @param condition Applied search condition. Optional.
	 * @return Access to the latest or earliest deployment which fulfills the specified condition, if defined
	 */
	def apply(direction: OrderDirection, condition: Option[Condition] = None): LatestOrEarliestDeploymentAccess =
		_Access(direction, condition)
	
	
	// NESTED   --------------------------
	
	private case class _Access(orderDirection: OrderDirection, accessCondition: Option[Condition])
		extends LatestOrEarliestDeploymentAccess
}

/**
 * Common interface for access points targeting the latest or the earliest deployment
 * which fulfills some condition (optionally)
 *
 * @author Mikko Hilpinen
 * @since 23.08.2024, v1.2
 */
trait LatestOrEarliestDeploymentAccess extends UniqueDeploymentAccess with LatestOrEarliestModelAccess[Deployment]
