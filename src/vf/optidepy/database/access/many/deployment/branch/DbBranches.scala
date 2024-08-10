package vf.optidepy.database.access.many.deployment.branch

import utopia.vault.nosql.view.{NonDeprecatedView, UnconditionalView, ViewManyByIntIds}
import vf.optidepy.model.stored.deployment.Branch

/**
  * The root access point when targeting multiple branches at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbBranches 
	extends ManyBranchesAccess with NonDeprecatedView[Branch] with ViewManyByIntIds[ManyBranchesAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * A copy of this access point that includes historical (i.e. deprecated) branches
	  */
	def includingHistory = DbBranchesIncludingHistory
	
	
	// NESTED	--------------------
	
	object DbBranchesIncludingHistory extends ManyBranchesAccess with UnconditionalView
}

