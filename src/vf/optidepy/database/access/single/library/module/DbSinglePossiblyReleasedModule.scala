package vf.optidepy.database.access.single.library.module

import utopia.vault.nosql.access.single.model.distinct.SingleIntIdModelAccess
import vf.optidepy.model.combined.library.PossiblyReleasedModule

/**
  * An access point to individual possibly released modules, based on their module id
  * @author Mikko Hilpinen
  * @since 24.08.2024, v1.2
  */
case class DbSinglePossiblyReleasedModule(id: Int) 
	extends UniquePossiblyReleasedModuleAccess with SingleIntIdModelAccess[PossiblyReleasedModule]

