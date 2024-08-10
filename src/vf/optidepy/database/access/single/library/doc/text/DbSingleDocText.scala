package vf.optidepy.database.access.single.library.doc.text

import utopia.vault.nosql.access.single.model.distinct.SingleIntIdModelAccess
import vf.optidepy.model.stored.library.DocText

/**
  * An access point to individual doc texts, based on their id
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DbSingleDocText(id: Int) extends UniqueDocTextAccess with SingleIntIdModelAccess[DocText]

