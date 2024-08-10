package vf.optidepy.database.access.single.deployment.branch

import utopia.vault.nosql.access.single.model.distinct.SingleIntIdModelAccess
import vf.optidepy.model.combined.deployment.PossiblyDeployedBranch

/**
  * An access point to individual possibly deployed branches, based on their branch id
  * @author Mikko Hilpinen
  * @since 10.08.2024, v1.2
  */
case class DbSinglePossiblyDeployedBranch(id: Int) 
	extends UniquePossiblyDeployedBranchAccess with SingleIntIdModelAccess[PossiblyDeployedBranch]

