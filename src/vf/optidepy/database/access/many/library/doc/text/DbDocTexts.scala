package vf.optidepy.database.access.many.library.doc.text

import utopia.vault.nosql.view.{UnconditionalView, ViewManyByIntIds}

/**
  * The root access point when targeting multiple doc texts at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbDocTexts extends ManyDocTextsAccess with UnconditionalView with ViewManyByIntIds[ManyDocTextsAccess]

