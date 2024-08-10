package vf.optidepy.database.access.single.dependency

import utopia.vault.nosql.access.single.model.distinct.SingleIntIdModelAccess
import vf.optidepy.model.combined.dependency.ProjectDependency

/**
  * An access point to individual project dependencies, based on their dependency id
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DbSingleProjectDependency(id: Int) 
	extends UniqueProjectDependencyAccess with SingleIntIdModelAccess[ProjectDependency]

