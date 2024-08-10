package vf.optidepy.database.access.single.library.doc.link

import utopia.vault.nosql.access.single.model.distinct.SingleIntIdModelAccess
import vf.optidepy.model.stored.library.SubSectionLink

/**
  * An access point to individual sub section links, based on their id
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DbSingleSubSectionLink(id: Int) 
	extends UniqueSubSectionLinkAccess with SingleIntIdModelAccess[SubSectionLink]

