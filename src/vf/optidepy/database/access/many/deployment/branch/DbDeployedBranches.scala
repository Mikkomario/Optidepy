package vf.optidepy.database.access.many.deployment.branch

import utopia.vault.nosql.view.{NonDeprecatedView, UnconditionalView, ViewManyByIntIds}
import vf.optidepy.model.combined.deployment.DeployedBranch

/**
  * The root access point when targeting multiple deployed branches at a time
  * @author Mikko Hilpinen
  * @since 10.08.2024, v1.2
  */
object DbDeployedBranches 
	extends ManyDeployedBranchesAccess with NonDeprecatedView[DeployedBranch] 
		with ViewManyByIntIds[ManyDeployedBranchesAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * A copy of this access point that includes historical (i.e. deprecated) deployed branches
	  */
	def includingHistory = DbDeployedBranchesIncludingHistory
	
	
	// NESTED	--------------------
	
	object DbDeployedBranchesIncludingHistory extends ManyDeployedBranchesAccess with UnconditionalView
}

