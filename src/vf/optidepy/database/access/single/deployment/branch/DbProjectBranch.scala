package vf.optidepy.database.access.single.deployment.branch

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.NonDeprecatedView
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.deployment.ProjectBranchDbFactory
import vf.optidepy.database.storable.deployment.BranchDbModel
import vf.optidepy.database.storable.project.ProjectDbModel
import vf.optidepy.model.combined.deployment.ProjectBranch

/**
  * Used for accessing individual project branchs
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbProjectBranch 
	extends SingleRowModelAccess[ProjectBranch] with NonDeprecatedView[ProjectBranch] with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * A database model (factory) used for interacting with the linked project
	  */
	protected def projectModel = ProjectDbModel
	
	/**
	  * A database model (factory) used for interacting with linked branches
	  */
	private def model = BranchDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = ProjectBranchDbFactory
	
	
	// OTHER	--------------------
	
	/**
	  * @param id Database id of the targeted project branch
	  * @return An access point to that project branch
	  */
	def apply(id: Int) = DbSingleProjectBranch(id)
	
	/**
	  * 
		@param additionalCondition Filter condition to apply in addition to this root view's condition. Should yield
	  *  unique project branchs.
	  * @return An access point to the project branch that satisfies the specified condition
	  */
	protected def filterDistinct(additionalCondition: Condition) = 
		UniqueProjectBranchAccess(mergeCondition(additionalCondition))
}

