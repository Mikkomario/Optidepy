package vf.optidepy.database.access.single.dependency.update

import utopia.vault.nosql.access.single.model.distinct.SingleIntIdModelAccess
import vf.optidepy.model.stored.dependency.DependencyUpdate

/**
  * An access point to individual dependency updates, based on their id
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DbSingleDependencyUpdate(id: Int) 
	extends UniqueDependencyUpdateAccess with SingleIntIdModelAccess[DependencyUpdate]

