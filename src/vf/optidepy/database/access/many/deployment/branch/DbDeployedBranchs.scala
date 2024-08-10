package vf.optidepy.database.access.many.deployment.branch

import utopia.vault.nosql.view.{NonDeprecatedView, UnconditionalView, ViewManyByIntIds}
import vf.optidepy.model.combined.deployment.DeployedBranch

/**
  * The root access point when targeting multiple deployed branchs at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbDeployedBranchs 
	extends ManyDeployedBranchsAccess with NonDeprecatedView[DeployedBranch] 
		with ViewManyByIntIds[ManyDeployedBranchsAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * A copy of this access point that includes historical (i.e. deprecated) deployed branchs
	  */
	def includingHistory = DbDeployedBranchsIncludingHistory
	
	
	// NESTED	--------------------
	
	object DbDeployedBranchsIncludingHistory extends ManyDeployedBranchsAccess with UnconditionalView
}

