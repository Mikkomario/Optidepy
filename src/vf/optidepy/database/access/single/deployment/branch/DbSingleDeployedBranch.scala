package vf.optidepy.database.access.single.deployment.branch

import utopia.vault.nosql.access.single.model.distinct.SingleIntIdModelAccess
import vf.optidepy.model.combined.deployment.DeployedBranch

/**
  * An access point to individual deployed branches, based on their branch id
  * @author Mikko Hilpinen
  * @since 10.08.2024, v1.2
  */
case class DbSingleDeployedBranch(id: Int) 
	extends UniqueDeployedBranchAccess with SingleIntIdModelAccess[DeployedBranch]

