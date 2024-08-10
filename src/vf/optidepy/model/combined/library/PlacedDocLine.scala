package vf.optidepy.model.combined.library

import utopia.flow.view.template.Extender
import utopia.vault.model.template.HasId
import vf.optidepy.model.factory.library.DocTextFactoryWrapper
import vf.optidepy.model.partial.library.DocTextData
import vf.optidepy.model.stored.library.{DocLineLink, DocText}

/**
  * Represents a specific text line which appears in a specific documentation section
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class PlacedDocLine(docText: DocText, link: DocLineLink) 
	extends Extender[DocTextData] with HasId[Int] with DocTextFactoryWrapper[DocText, PlacedDocLine]
{
	// COMPUTED	--------------------
	
	/**
	  * Id of this doc text in the database
	  */
	def id = docText.id
	
	
	// IMPLEMENTED	--------------------
	
	override def wrapped = docText.data
	
	override protected def wrappedFactory = docText
	
	override protected def wrap(factory: DocText) = copy(docText = factory)
}

