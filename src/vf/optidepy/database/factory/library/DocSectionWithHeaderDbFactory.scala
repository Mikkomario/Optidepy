package vf.optidepy.database.factory.library

import utopia.vault.nosql.factory.row.linked.PossiblyCombiningFactory
import vf.optidepy.model.combined.library.DocSectionWithHeader
import vf.optidepy.model.stored.library.{DocSection, DocText}

/**
  * Used for reading doc section with headers from the database
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DocSectionWithHeaderDbFactory 
	extends PossiblyCombiningFactory[DocSectionWithHeader, DocSection, DocText]
{
	// IMPLEMENTED	--------------------
	
	override def childFactory = DocTextDbFactory
	
	override def parentFactory = DocSectionDbFactory
	
	override def apply(section: DocSection, header: Option[DocText]) = DocSectionWithHeader(section, header)
}

