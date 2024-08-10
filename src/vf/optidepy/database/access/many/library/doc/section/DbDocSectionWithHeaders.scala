package vf.optidepy.database.access.many.library.doc.section

import utopia.vault.nosql.view.{UnconditionalView, ViewManyByIntIds}

/**
  * The root access point when targeting multiple doc section with headers at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbDocSectionWithHeaders 
	extends ManyDocSectionWithHeadersAccess with UnconditionalView 
		with ViewManyByIntIds[ManyDocSectionWithHeadersAccess]

