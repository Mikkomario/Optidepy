package vf.optidepy.database.access.many.project

import utopia.vault.nosql.access.many.model.ManyRowModelAccess
import utopia.vault.nosql.view.ViewFactory
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.project.ProjectDbFactory
import vf.optidepy.model.stored.project.Project

object ManyProjectsAccess extends ViewFactory[ManyProjectsAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): ManyProjectsAccess = _ManyProjectsAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _ManyProjectsAccess(override val accessCondition: Option[Condition]) 
		extends ManyProjectsAccess
}

/**
  * A common trait for access points which target multiple projects at a time
  * @author Mikko Hilpinen
  * @since 12.08.2024, v1.2
  */
trait ManyProjectsAccess 
	extends ManyProjectsAccessLike[Project, ManyProjectsAccess] with ManyRowModelAccess[Project]
{
	// IMPLEMENTED	--------------------
	
	override def factory = ProjectDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): ManyProjectsAccess = ManyProjectsAccess(condition)
}

