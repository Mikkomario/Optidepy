package vf.optidepy.database.access.many.library.doc.link

import utopia.vault.nosql.view.{UnconditionalView, ViewManyByIntIds}

/**
  * The root access point when targeting multiple doc line links at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbDocLineLinks 
	extends ManyDocLineLinksAccess with UnconditionalView with ViewManyByIntIds[ManyDocLineLinksAccess]

