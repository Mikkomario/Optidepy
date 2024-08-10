package vf.optidepy.database.access.single.library.doc.text

import utopia.vault.nosql.access.single.model.distinct.SingleIntIdModelAccess
import vf.optidepy.model.combined.library.PlacedDocLine

/**
  * An access point to individual placed doc lines, based on their doc text id
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DbSinglePlacedDocLine(id: Int) 
	extends UniquePlacedDocLineAccess with SingleIntIdModelAccess[PlacedDocLine]

