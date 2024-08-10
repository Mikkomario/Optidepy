package vf.optidepy.database.access.many.library.doc.section

import utopia.vault.nosql.view.{UnconditionalView, ViewManyByIntIds}

/**
  * The root access point when targeting multiple doc sections at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbDocSections 
	extends ManyDocSectionsAccess with UnconditionalView with ViewManyByIntIds[ManyDocSectionsAccess]

