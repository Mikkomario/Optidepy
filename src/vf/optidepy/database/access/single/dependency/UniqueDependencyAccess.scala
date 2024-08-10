package vf.optidepy.database.access.single.dependency

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.view.{NullDeprecatableView, ViewFactory}
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.dependency.DependencyDbFactory
import vf.optidepy.model.stored.dependency.Dependency

object UniqueDependencyAccess extends ViewFactory[UniqueDependencyAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override
		 def apply(condition: Condition): UniqueDependencyAccess = _UniqueDependencyAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _UniqueDependencyAccess(override val accessCondition: Option[Condition]) 
		extends UniqueDependencyAccess
}

/**
  * A common trait for access points that return individual and distinct dependencies.
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait UniqueDependencyAccess 
	extends UniqueDependencyAccessLike[Dependency, UniqueDependencyAccess] 
		with SingleRowModelAccess[Dependency] with NullDeprecatableView[UniqueDependencyAccess]
{
	// IMPLEMENTED	--------------------
	
	override def factory = DependencyDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): UniqueDependencyAccess = UniqueDependencyAccess(condition)
}

