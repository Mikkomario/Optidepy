package vf.optidepy.database.access.single.library.doc.link

import utopia.vault.nosql.access.single.model.distinct.SingleIntIdModelAccess
import vf.optidepy.model.stored.library.DocLineLink

/**
  * An access point to individual doc line links, based on their id
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DbSingleDocLineLink(id: Int) 
	extends UniqueDocLineLinkAccess with SingleIntIdModelAccess[DocLineLink]

