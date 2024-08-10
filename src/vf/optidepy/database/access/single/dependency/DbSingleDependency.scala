package vf.optidepy.database.access.single.dependency

import utopia.vault.nosql.access.single.model.distinct.SingleIntIdModelAccess
import vf.optidepy.model.stored.dependency.Dependency

/**
  * An access point to individual dependencies, based on their id
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DbSingleDependency(id: Int) extends UniqueDependencyAccess with SingleIntIdModelAccess[Dependency]

