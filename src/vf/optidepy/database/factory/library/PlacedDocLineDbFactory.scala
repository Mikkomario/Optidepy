package vf.optidepy.database.factory.library

import utopia.vault.nosql.factory.row.linked.CombiningFactory
import vf.optidepy.model.combined.library.PlacedDocLine
import vf.optidepy.model.stored.library.{DocLineLink, DocText}

/**
  * Used for reading placed doc lines from the database
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object PlacedDocLineDbFactory extends CombiningFactory[PlacedDocLine, DocText, DocLineLink]
{
	// IMPLEMENTED	--------------------
	
	override def childFactory = DocLineLinkDbFactory
	
	override def parentFactory = DocTextDbFactory
	
	override def apply(docText: DocText, link: DocLineLink) = PlacedDocLine(docText, link)
}

