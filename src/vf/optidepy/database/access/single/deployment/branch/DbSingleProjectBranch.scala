package vf.optidepy.database.access.single.deployment.branch

import utopia.vault.nosql.access.single.model.distinct.SingleIntIdModelAccess
import vf.optidepy.model.combined.deployment.ProjectBranch

/**
  * An access point to individual project branchs, based on their branch id
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DbSingleProjectBranch(id: Int) 
	extends UniqueProjectBranchAccess with SingleIntIdModelAccess[ProjectBranch]

