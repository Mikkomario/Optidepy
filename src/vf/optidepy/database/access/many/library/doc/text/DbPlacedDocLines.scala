package vf.optidepy.database.access.many.library.doc.text

import utopia.vault.nosql.view.{UnconditionalView, ViewManyByIntIds}

/**
  * The root access point when targeting multiple placed doc lines at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbPlacedDocLines 
	extends ManyPlacedDocLinesAccess with UnconditionalView with ViewManyByIntIds[ManyPlacedDocLinesAccess]

