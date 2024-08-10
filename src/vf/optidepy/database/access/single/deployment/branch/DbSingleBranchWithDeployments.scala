package vf.optidepy.database.access.single.deployment.branch

import utopia.vault.nosql.access.single.model.distinct.SingleIntIdModelAccess
import vf.optidepy.model.combined.deployment.BranchWithDeployments

/**
  * An access point to individual branches with deployments, based on their branch id
  * @author Mikko Hilpinen
  * @since 10.08.2024, v1.2
  */
case class DbSingleBranchWithDeployments(id: Int) 
	extends UniqueBranchWithDeploymentsAccess with SingleIntIdModelAccess[BranchWithDeployments]

