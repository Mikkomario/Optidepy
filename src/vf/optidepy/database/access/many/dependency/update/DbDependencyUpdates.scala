package vf.optidepy.database.access.many.dependency.update

import utopia.vault.nosql.view.{UnconditionalView, ViewManyByIntIds}

/**
  * The root access point when targeting multiple dependency updates at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbDependencyUpdates 
	extends ManyDependencyUpdatesAccess with UnconditionalView 
		with ViewManyByIntIds[ManyDependencyUpdatesAccess]

