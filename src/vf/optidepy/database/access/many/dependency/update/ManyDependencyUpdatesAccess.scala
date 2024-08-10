package vf.optidepy.database.access.many.dependency.update

import utopia.flow.generic.casting.ValueConversions._
import utopia.vault.database.Connection
import utopia.vault.nosql.access.many.model.ManyRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.{ChronoRowFactoryView, ViewFactory}
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.dependency.DependencyUpdateDbFactory
import vf.optidepy.database.storable.dependency.DependencyUpdateDbModel
import vf.optidepy.model.stored.dependency.DependencyUpdate

object ManyDependencyUpdatesAccess extends ViewFactory[ManyDependencyUpdatesAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): ManyDependencyUpdatesAccess = 
		_ManyDependencyUpdatesAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _ManyDependencyUpdatesAccess(override val accessCondition: Option[Condition]) 
		extends ManyDependencyUpdatesAccess
}

/**
  * A common trait for access points which target multiple dependency updates at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait ManyDependencyUpdatesAccess 
	extends ManyRowModelAccess[DependencyUpdate] 
		with ChronoRowFactoryView[DependencyUpdate, ManyDependencyUpdatesAccess] with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * dependency ids of the accessible dependency updates
	  */
	def dependencyIds(implicit connection: Connection) = 
		pullColumn(model.dependencyId.column).map { v => v.getInt }
	
	/**
	  * release ids of the accessible dependency updates
	  */
	def releaseIds(implicit connection: Connection) = pullColumn(model.releaseId.column).map { v => v.getInt }
	
	/**
	  * creation times of the accessible dependency updates
	  */
	def creationTimes(implicit connection: Connection) = 
		pullColumn(model.created.column).map { v => v.getInstant }
	
	/**
	  * Unique ids of the accessible dependency updates
	  */
	def ids(implicit connection: Connection) = pullColumn(index).map { v => v.getInt }
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	protected def model = DependencyUpdateDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = DependencyUpdateDbFactory
	
	override protected def self = this
	
	override
		 def apply(condition: Condition): ManyDependencyUpdatesAccess = ManyDependencyUpdatesAccess(condition)
	
	
	// OTHER	--------------------
	
	/**
	  * @param dependencyIds Targeted dependency ids
	  * 
		@return Copy of this access point that only includes dependency updates where dependency id is within the
	  *  specified value set
	  */
	def ofDependencies(dependencyIds: Iterable[Int]) = filter(model.dependencyId.column.in(dependencyIds))
	
	/**
	  * @param dependencyId dependency id to target
	  * @return Copy of this access point that only includes dependency updates 
		with the specified dependency id
	  */
	def ofDependency(dependencyId: Int) = filter(model.dependencyId.column <=> dependencyId)
	
	/**
	  * @param releaseId release id to target
	  * @return Copy of this access point that only includes dependency updates with the specified release id
	  */
	def toRelease(releaseId: Int) = filter(model.releaseId.column <=> releaseId)
	
	/**
	  * @param releaseIds Targeted release ids
	  * @return Copy of this access point that only includes dependency updates where release id is within the
	  *  specified value set
	  */
	def toReleases(releaseIds: Iterable[Int]) = filter(model.releaseId.column.in(releaseIds))
}

