package vf.optidepy.database.access.single.library.module

import utopia.vault.nosql.access.single.model.distinct.SingleIntIdModelAccess
import vf.optidepy.model.combined.library.ReleasedModule

/**
  * An access point to individual released modules, based on their module id
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DbSingleReleasedModule(id: Int) 
	extends UniqueReleasedModuleAccess with SingleIntIdModelAccess[ReleasedModule]

