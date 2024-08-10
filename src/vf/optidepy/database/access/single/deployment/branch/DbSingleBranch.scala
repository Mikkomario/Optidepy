package vf.optidepy.database.access.single.deployment.branch

import utopia.vault.nosql.access.single.model.distinct.SingleIntIdModelAccess
import vf.optidepy.model.stored.deployment.Branch

/**
  * An access point to individual branches, based on their id
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DbSingleBranch(id: Int) extends UniqueBranchAccess with SingleIntIdModelAccess[Branch]

