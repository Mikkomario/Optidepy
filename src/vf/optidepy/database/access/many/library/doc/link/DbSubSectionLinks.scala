package vf.optidepy.database.access.many.library.doc.link

import utopia.vault.nosql.view.{UnconditionalView, ViewManyByIntIds}

/**
  * The root access point when targeting multiple sub section links at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbSubSectionLinks 
	extends ManySubSectionLinksAccess with UnconditionalView with ViewManyByIntIds[ManySubSectionLinksAccess]

