package vf.optidepy.database.access.many.deployment.branch

import utopia.vault.nosql.view.{NonDeprecatedView, UnconditionalView, ViewManyByIntIds}
import vf.optidepy.model.combined.deployment.BranchWithDeployments

/**
  * The root access point when targeting multiple branches with deployments at a time
  * @author Mikko Hilpinen
  * @since 10.08.2024, v1.2
  */
object DbBranchesWithDeployments 
	extends ManyBranchesWithDeploymentsAccess with NonDeprecatedView[BranchWithDeployments] 
		with ViewManyByIntIds[ManyBranchesWithDeploymentsAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * A copy of this access point that includes historical (i.e. deprecated) branches with deployments
	  */
	def includingHistory = DbBranchesWithDeploymentsIncludingHistory
	
	
	// NESTED	--------------------
	
	object DbBranchesWithDeploymentsIncludingHistory 
		extends ManyBranchesWithDeploymentsAccess with UnconditionalView
}

