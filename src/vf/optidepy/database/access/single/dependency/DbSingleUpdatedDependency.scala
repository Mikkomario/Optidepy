package vf.optidepy.database.access.single.dependency

import utopia.vault.nosql.access.single.model.distinct.SingleIntIdModelAccess
import vf.optidepy.model.combined.dependency.UpdatedDependency

/**
  * An access point to individual updated dependencies, based on their dependency id
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DbSingleUpdatedDependency(id: Int) 
	extends UniqueUpdatedDependencyAccess with SingleIntIdModelAccess[UpdatedDependency]

