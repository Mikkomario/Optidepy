package vf.optidepy.database.access.many.deployment.binding

import utopia.vault.nosql.view.{UnconditionalView, ViewManyByIntIds}

/**
  * The root access point when targeting multiple bindings at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbBindings extends ManyBindingsAccess with UnconditionalView with ViewManyByIntIds[ManyBindingsAccess]

