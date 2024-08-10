package vf.optidepy.database.access.single.library.doc.section

import utopia.vault.nosql.access.single.model.distinct.SingleIntIdModelAccess
import vf.optidepy.model.combined.library.SubSection

/**
  * An access point to individual sub sections, based on their section id
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DbSingleSubSection(id: Int) extends UniqueSubSectionAccess with SingleIntIdModelAccess[SubSection]

