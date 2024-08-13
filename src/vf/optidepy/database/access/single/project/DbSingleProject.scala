package vf.optidepy.database.access.single.project

import utopia.vault.nosql.access.single.model.distinct.SingleIntIdModelAccess
import vf.optidepy.model.stored.project.Project

/**
  * An access point to individual projects, based on their id
  * @author Mikko Hilpinen
  * @since 12.08.2024, v1.2
  */
case class DbSingleProject(id: Int) extends UniqueProjectAccess with SingleIntIdModelAccess[Project]

