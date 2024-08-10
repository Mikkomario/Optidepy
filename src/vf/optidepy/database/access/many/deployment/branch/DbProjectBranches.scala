package vf.optidepy.database.access.many.deployment.branch

import utopia.vault.nosql.view.{NonDeprecatedView, UnconditionalView, ViewManyByIntIds}
import vf.optidepy.model.combined.deployment.ProjectBranch

/**
  * The root access point when targeting multiple project branches at a time
  * @author Mikko Hilpinen
  * @since 10.08.2024, v1.2
  */
object DbProjectBranches 
	extends ManyProjectBranchesAccess with NonDeprecatedView[ProjectBranch] 
		with ViewManyByIntIds[ManyProjectBranchesAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * A copy of this access point that includes historical (i.e. deprecated) project branches
	  */
	def includingHistory = DbProjectBranchesIncludingHistory
	
	
	// NESTED	--------------------
	
	object DbProjectBranchesIncludingHistory extends ManyProjectBranchesAccess with UnconditionalView
}

