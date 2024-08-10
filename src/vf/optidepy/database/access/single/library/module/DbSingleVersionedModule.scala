package vf.optidepy.database.access.single.library.module

import utopia.vault.nosql.access.single.model.distinct.SingleIntIdModelAccess
import vf.optidepy.model.stored.library.VersionedModule

/**
  * An access point to individual versioned modules, based on their id
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DbSingleVersionedModule(id: Int) 
	extends UniqueVersionedModuleAccess with SingleIntIdModelAccess[VersionedModule]

