package vf.optidepy.database.access.many.library.module.release

import utopia.vault.nosql.view.{UnconditionalView, ViewManyByIntIds}

/**
  * The root access point when targeting multiple module releases at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbModuleReleases 
	extends ManyModuleReleasesAccess with UnconditionalView with ViewManyByIntIds[ManyModuleReleasesAccess]

