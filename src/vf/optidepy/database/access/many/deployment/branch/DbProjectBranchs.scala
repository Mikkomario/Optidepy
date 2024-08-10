package vf.optidepy.database.access.many.deployment.branch

import utopia.vault.nosql.view.{NonDeprecatedView, UnconditionalView, ViewManyByIntIds}
import vf.optidepy.model.combined.deployment.ProjectBranch

/**
  * The root access point when targeting multiple project branchs at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbProjectBranchs 
	extends ManyProjectBranchsAccess with NonDeprecatedView[ProjectBranch] 
		with ViewManyByIntIds[ManyProjectBranchsAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * A copy of this access point that includes historical (i.e. deprecated) project branchs
	  */
	def includingHistory = DbProjectBranchsIncludingHistory
	
	
	// NESTED	--------------------
	
	object DbProjectBranchsIncludingHistory extends ManyProjectBranchsAccess with UnconditionalView
}

