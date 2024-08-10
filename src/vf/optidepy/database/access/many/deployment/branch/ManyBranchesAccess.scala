package vf.optidepy.database.access.many.deployment.branch

import utopia.vault.nosql.access.many.model.ManyRowModelAccess
import utopia.vault.nosql.view.ViewFactory
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.deployment.BranchDbFactory
import vf.optidepy.model.stored.deployment.Branch

object ManyBranchesAccess extends ViewFactory[ManyBranchesAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): ManyBranchesAccess = _ManyBranchesAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _ManyBranchesAccess(override val accessCondition: Option[Condition]) 
		extends ManyBranchesAccess
}

/**
  * A common trait for access points which target multiple branches at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait ManyBranchesAccess 
	extends ManyBranchesAccessLike[Branch, ManyBranchesAccess] with ManyRowModelAccess[Branch]
{
	// IMPLEMENTED	--------------------
	
	override def factory = BranchDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): ManyBranchesAccess = ManyBranchesAccess(condition)
}

