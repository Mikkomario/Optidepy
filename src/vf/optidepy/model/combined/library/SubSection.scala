package vf.optidepy.model.combined.library

import utopia.flow.view.template.Extender
import utopia.vault.model.template.HasId
import vf.optidepy.model.factory.library.DocSectionFactoryWrapper
import vf.optidepy.model.partial.library.DocSectionData
import vf.optidepy.model.stored.library.{DocSection, SubSectionLink}

/**
  * Represents a documentation section which appears as a subsection of another section.
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class SubSection(section: DocSection, link: Option[SubSectionLink]) 
	extends Extender[DocSectionData] with HasId[Int] with DocSectionFactoryWrapper[DocSection, SubSection]
{
	// COMPUTED	--------------------
	
	/**
	  * Id of this section in the database
	  */
	def id = section.id
	
	
	// IMPLEMENTED	--------------------
	
	override def wrapped = section.data
	
	override protected def wrappedFactory = section
	
	override protected def wrap(factory: DocSection) = copy(section = factory)
}

