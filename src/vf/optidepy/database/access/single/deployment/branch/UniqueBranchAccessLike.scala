package vf.optidepy.database.access.single.deployment.branch

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.model.immutable.Value
import utopia.vault.database.Connection
import utopia.vault.nosql.access.single.model.SingleModelAccess
import utopia.vault.nosql.access.template.model.DistinctModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.FilterableView
import vf.optidepy.database.storable.deployment.BranchDbModel

import java.time.Instant

/**
  * A common trait for access points which target individual branches or similar items at a time
  * @tparam A Type of read (branches -like) instances
  * @tparam Repr Type of this access point
  * @author Mikko Hilpinen
  * @since 23.08.2024, v1.2
  */
trait UniqueBranchAccessLike[+A, +Repr] 
	extends SingleModelAccess[A] with DistinctModelAccess[A, Option[A], Value] with FilterableView[Repr] 
		with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * Id of the deployment configuration which this branch versions. 
	  * None if no branch (or value) was found.
	  */
	def deploymentConfigId(implicit connection: Connection) = pullColumn(model.deploymentConfigId.column).int
	
	/**
	  * Name of this branch. 
	  * None if no branch (or value) was found.
	  */
	def name(implicit connection: Connection) = pullColumn(model.name.column).getString
	
	/**
	  * Time when this branch was introduced. 
	  * None if no branch (or value) was found.
	  */
	def created(implicit connection: Connection) = pullColumn(model.created.column).instant
	
	/**
	  * Time when this branch was removed. 
	  * None if no branch (or value) was found.
	  */
	def deprecatedAfter(implicit connection: Connection) = pullColumn(model.deprecatedAfter.column).instant
	
	/**
	  * Whether this is the default branch of the associated project. 
	  * None if no branch (or value) was found.
	  */
	def isDefault(implicit connection: Connection) = pullColumn(model.isDefault.column).boolean
	
	/**
	  * Unique id of the accessible branch. None if no branch was accessible.
	  */
	def id(implicit connection: Connection) = pullColumn(index).int
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	protected def model = BranchDbModel
	
	
	// OTHER	--------------------
	
	/**
	  * Updates the deprecation times of the targeted branches
	  * @param newDeprecatedAfter A new deprecated after to assign
	  * @return Whether any branch was affected
	  */
	def deprecatedAfter_=(newDeprecatedAfter: Instant)(implicit connection: Connection) = 
		putColumn(model.deprecatedAfter.column, newDeprecatedAfter)
}

