package vf.optidepy.database.access.single.dependency.update

import utopia.flow.generic.model.immutable.Value
import utopia.vault.database.Connection
import utopia.vault.nosql.access.single.model.SingleChronoRowModelAccess
import utopia.vault.nosql.access.template.model.DistinctModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.{FilterableView, ViewFactory}
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.dependency.DependencyUpdateDbFactory
import vf.optidepy.database.storable.dependency.DependencyUpdateDbModel
import vf.optidepy.model.stored.dependency.DependencyUpdate

object UniqueDependencyUpdateAccess extends ViewFactory[UniqueDependencyUpdateAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): UniqueDependencyUpdateAccess = 
		_UniqueDependencyUpdateAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _UniqueDependencyUpdateAccess(override val accessCondition: Option[Condition]) 
		extends UniqueDependencyUpdateAccess
}

/**
  * A common trait for access points that return individual and distinct dependency updates.
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait UniqueDependencyUpdateAccess 
	extends SingleChronoRowModelAccess[DependencyUpdate, UniqueDependencyUpdateAccess] 
		with DistinctModelAccess[DependencyUpdate, Option[DependencyUpdate], Value] 
		with FilterableView[UniqueDependencyUpdateAccess] with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * Id of the dependency this update concerns. 
	  * None if no dependency update (or value) was found.
	  */
	def dependencyId(implicit connection: Connection) = pullColumn(model.dependencyId.column).int
	
	/**
	  * Id of the library release that was to the parent project. 
	  * None if no dependency update (or value) was found.
	  */
	def releaseId(implicit connection: Connection) = pullColumn(model.releaseId.column).int
	
	/**
	  * Time when this update was made. 
	  * None if no dependency update (or value) was found.
	  */
	def created(implicit connection: Connection) = pullColumn(model.created.column).instant
	
	/**
	  * Unique id of the accessible dependency update. None if no dependency update was accessible.
	  */
	def id(implicit connection: Connection) = pullColumn(index).int
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	protected def model = DependencyUpdateDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = DependencyUpdateDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): UniqueDependencyUpdateAccess = 
		UniqueDependencyUpdateAccess(condition)
}

