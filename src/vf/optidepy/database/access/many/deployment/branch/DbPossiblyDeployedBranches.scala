package vf.optidepy.database.access.many.deployment.branch

import utopia.vault.nosql.view.{NonDeprecatedView, UnconditionalView, ViewManyByIntIds}
import vf.optidepy.model.combined.deployment.PossiblyDeployedBranch

/**
  * The root access point when targeting multiple possibly deployed branches at a time
  * @author Mikko Hilpinen
  * @since 10.08.2024, v1.2
  */
object DbPossiblyDeployedBranches 
	extends ManyPossiblyDeployedBranchesAccess with NonDeprecatedView[PossiblyDeployedBranch] 
		with ViewManyByIntIds[ManyPossiblyDeployedBranchesAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * A copy of this access point that includes historical (i.e. deprecated) possibly deployed branches
	  */
	def includingHistory = DbPossiblyDeployedBranchesIncludingHistory
	
	
	// NESTED	--------------------
	
	object DbPossiblyDeployedBranchesIncludingHistory 
		extends ManyPossiblyDeployedBranchesAccess with UnconditionalView
}

