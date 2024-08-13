package vf.optidepy.database.access.many.dependency

import utopia.vault.nosql.access.many.model.ManyRowModelAccess
import utopia.vault.nosql.view.ViewFactory
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.dependency.DependencyDbFactory
import vf.optidepy.model.stored.dependency.Dependency

object ManyDependenciesAccess extends ViewFactory[ManyDependenciesAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): ManyDependenciesAccess = _ManyDependenciesAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _ManyDependenciesAccess(override val accessCondition: Option[Condition]) 
		extends ManyDependenciesAccess
}

/**
  * A common trait for access points which target multiple dependencies at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait ManyDependenciesAccess 
	extends ManyDependenciesAccessLike[Dependency, ManyDependenciesAccess] with ManyRowModelAccess[Dependency]
{
	// IMPLEMENTED	--------------------
	
	override def factory = DependencyDbFactory
	override protected def self = this
	
	override def apply(condition: Condition): ManyDependenciesAccess = ManyDependenciesAccess(condition)
}

