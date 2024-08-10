package vf.optidepy.database.access.single.library.doc.section

import utopia.vault.nosql.access.single.model.distinct.SingleIntIdModelAccess
import vf.optidepy.model.stored.library.DocSection

/**
  * An access point to individual doc sections, based on their id
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DbSingleDocSection(id: Int) extends UniqueDocSectionAccess with SingleIntIdModelAccess[DocSection]

