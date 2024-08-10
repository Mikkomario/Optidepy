package vf.optidepy.database.access.single.deployment.branch

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.NonDeprecatedView
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.deployment.BranchDbFactory
import vf.optidepy.database.storable.deployment.BranchDbModel
import vf.optidepy.model.stored.deployment.Branch

/**
  * Used for accessing individual branches
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbBranch extends SingleRowModelAccess[Branch] with NonDeprecatedView[Branch] with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	protected def model = BranchDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = BranchDbFactory
	
	
	// OTHER	--------------------
	
	/**
	  * @param id Database id of the targeted branch
	  * @return An access point to that branch
	  */
	def apply(id: Int) = DbSingleBranch(id)
	
	/**
	  * 
		@param additionalCondition Filter condition to apply in addition to this root view's condition. Should yield
	  *  unique branches.
	  * @return An access point to the branch that satisfies the specified condition
	  */
	protected def filterDistinct(additionalCondition: Condition) = 
		UniqueBranchAccess(mergeCondition(additionalCondition))
}

