package vf.optidepy.database.factory.library

import utopia.vault.nosql.factory.row.linked.CombiningFactory
import vf.optidepy.model.combined.library.SubSection
import vf.optidepy.model.stored.library.{DocSection, SubSectionLink}

/**
  * Used for reading sub sections from the database
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object SubSectionDbFactory extends CombiningFactory[SubSection, DocSection, SubSectionLink]
{
	// IMPLEMENTED	--------------------
	
	override def childFactory = SubSectionLinkDbFactory
	
	override def parentFactory = DocSectionDbFactory
	
	override def apply(section: DocSection, link: SubSectionLink) = SubSection(section, link)
}

