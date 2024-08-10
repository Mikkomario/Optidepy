package vf.optidepy.database.access.single.deployment.branch

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.view.{NullDeprecatableView, ViewFactory}
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.deployment.BranchDbFactory
import vf.optidepy.model.stored.deployment.Branch

object UniqueBranchAccess extends ViewFactory[UniqueBranchAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): UniqueBranchAccess = _UniqueBranchAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _UniqueBranchAccess(override val accessCondition: Option[Condition]) 
		extends UniqueBranchAccess
}

/**
  * A common trait for access points that return individual and distinct branches.
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait UniqueBranchAccess 
	extends UniqueBranchAccessLike[Branch, UniqueBranchAccess] with SingleRowModelAccess[Branch] 
		with NullDeprecatableView[UniqueBranchAccess]
{
	// IMPLEMENTED	--------------------
	
	override def factory = BranchDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): UniqueBranchAccess = UniqueBranchAccess(condition)
}

