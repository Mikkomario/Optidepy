package vf.optidepy.database.access.many.deployment.branch

import utopia.flow.generic.casting.ValueConversions._
import utopia.vault.database.Connection
import utopia.vault.nosql.access.many.model.ManyModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.NullDeprecatableView
import vf.optidepy.database.storable.deployment.BranchDbModel

import java.time.Instant

/**
  * A common trait for access points which target multiple branches or similar instances at a time
  * @tparam A Type of read (branches -like) instances
  * @tparam Repr Type of this access point
  * @author Mikko Hilpinen
  * @since 23.08.2024, v1.2
  */
trait ManyBranchesAccessLike[+A, +Repr] 
	extends ManyModelAccess[A] with Indexed with NullDeprecatableView[Repr]
{
	// COMPUTED	--------------------
	
	/**
	  * deployment config ids of the accessible branches
	  */
	def deploymentConfigIds(implicit connection: Connection) = 
		pullColumn(model.deploymentConfigId.column).map { v => v.getInt }
	
	/**
	  * names of the accessible branches
	  */
	def names(implicit connection: Connection) = pullColumn(model.name.column).flatMap { _.string }
	
	/**
	  * creation times of the accessible branches
	  */
	def creationTimes(implicit connection: Connection) = 
		pullColumn(model.created.column).map { v => v.getInstant }
	
	/**
	  * deprecation times of the accessible branches
	  */
	def deprecationTimes(implicit connection: Connection) = 
		pullColumn(model.deprecatedAfter.column).flatMap { v => v.instant }
	
	/**
	  * are defaults of the accessible branches
	  */
	def areDefaults(implicit connection: Connection) = 
		pullColumn(model.isDefault.column).map { v => v.getBoolean }
	
	/**
	  * Unique ids of the accessible branches
	  */
	def ids(implicit connection: Connection) = pullColumn(index).map { v => v.getInt }
	
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
	def deprecationTimes_=(newDeprecatedAfter: Instant)(implicit connection: Connection) = 
		putColumn(model.deprecatedAfter.column, newDeprecatedAfter)
	
	/**
	  * @param deploymentConfigId deployment config id to target
	  * @return Copy of this access point that only includes branches with the specified deployment config id
	  */
	def ofDeploymentConfig(deploymentConfigId: Int) = 
		filter(model.deploymentConfigId.column <=> deploymentConfigId)
	
	/**
	  * @param deploymentConfigIds Targeted deployment config ids
	  * @return Copy of this access point that only includes branches where deployment config id is within the
	  *  specified value set
	  */
	def ofDeploymentConfigs(deploymentConfigIds: Iterable[Int]) = 
		filter(model.deploymentConfigId.column.in(deploymentConfigIds))
	
	/**
	  * @param areDefaults Targeted are defaults
	  * @return Copy of this access point that only includes branches where is default is within the specified
	  *  value set
	  */
	def withAreDefaults(areDefaults: Iterable[Boolean]) = filter(model.isDefault.column.in(areDefaults))
	
	/**
	  * @param isDefault is default to target
	  * @return Copy of this access point that only includes branches with the specified is default
	  */
	def withIsDefault(isDefault: Boolean) = filter(model.isDefault.column <=> isDefault)
	
	/**
	  * @param name name to target
	  * @return Copy of this access point that only includes branches with the specified name
	  */
	def withName(name: String) = filter(model.name.column <=> name)
	
	/**
	  * @param names Targeted names
	  * 
		@return Copy of this access point that only includes branches where name is within the specified value set
	  */
	def withNames(names: Iterable[String]) = filter(model.name.column.in(names))
}

