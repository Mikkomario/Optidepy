package vf.optidepy.database.access.single.library.module.release

import utopia.vault.nosql.access.single.model.distinct.SingleIntIdModelAccess
import vf.optidepy.model.stored.library.ModuleRelease

/**
  * An access point to individual module releases, based on their id
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DbSingleModuleRelease(id: Int) 
	extends UniqueModuleReleaseAccess with SingleIntIdModelAccess[ModuleRelease]

