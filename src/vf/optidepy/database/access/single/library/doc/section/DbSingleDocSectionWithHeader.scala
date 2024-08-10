package vf.optidepy.database.access.single.library.doc.section

import utopia.vault.nosql.access.single.model.distinct.SingleIntIdModelAccess
import vf.optidepy.model.combined.library.DocSectionWithHeader

/**
  * An access point to individual doc section with headers, based on their section id
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DbSingleDocSectionWithHeader(id: Int) 
	extends UniqueDocSectionWithHeaderAccess with SingleIntIdModelAccess[DocSectionWithHeader]

