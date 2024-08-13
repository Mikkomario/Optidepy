package vf.optidepy.database.access.single.project

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.view.{NullDeprecatableView, ViewFactory}
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.project.ProjectDbFactory
import vf.optidepy.model.stored.project.Project

object UniqueProjectAccess extends ViewFactory[UniqueProjectAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): UniqueProjectAccess = _UniqueProjectAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _UniqueProjectAccess(override val accessCondition: Option[Condition]) 
		extends UniqueProjectAccess
}

/**
  * A common trait for access points that return individual and distinct projects.
  * @author Mikko Hilpinen
  * @since 12.08.2024, v1.2
  */
trait UniqueProjectAccess 
	extends UniqueProjectAccessLike[Project, UniqueProjectAccess] with SingleRowModelAccess[Project] 
		with NullDeprecatableView[UniqueProjectAccess]
{
	// IMPLEMENTED	--------------------
	
	override def factory = ProjectDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): UniqueProjectAccess = UniqueProjectAccess(condition)
}

