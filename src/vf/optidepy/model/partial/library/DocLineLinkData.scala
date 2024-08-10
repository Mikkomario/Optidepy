package vf.optidepy.model.partial.library

import utopia.flow.collection.immutable.Single
import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.factory.FromModelFactoryWithSchema
import utopia.flow.generic.model.immutable.{Model, ModelDeclaration, PropertyDeclaration}
import utopia.flow.generic.model.mutable.DataType.IntType
import vf.optidepy.model.factory.library.DocLineLinkFactory

object DocLineLinkData extends FromModelFactoryWithSchema[DocLineLinkData]
{
	// ATTRIBUTES	--------------------
	
	override lazy val schema = 
		ModelDeclaration(Vector(PropertyDeclaration("sectionId", IntType, Vector("parentId", "parent_id", 
			"section_id")), PropertyDeclaration("textId", IntType, Vector("childId", "child_id", "text_id")), 
			PropertyDeclaration("orderIndex", IntType, Single("order_index"), 0)))
	
	
	// IMPLEMENTED	--------------------
	
	override protected def fromValidatedModel(valid: Model) = 
		DocLineLinkData(valid("sectionId").getInt, valid("textId").getInt, valid("orderIndex").getInt)
}

/**
  * Links a line of text to a document section it appears in
  * @param sectionId Id of the doc section linked with this doc line link
  * @param textId Id of the doc text linked with this doc line link
  * @param orderIndex 0-based index determining the location where the linked item is placed
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DocLineLinkData(sectionId: Int, textId: Int, orderIndex: Int = 0) 
	extends PlacedLinkData with PlacedLinkDataLike[DocLineLinkData]
		with DocLineLinkFactory[DocLineLinkData]
{
	// IMPLEMENTED	--------------------
	
	override def childId = textId
	override def parentId = sectionId
	
	override def withSectionId(sectionId: Int): DocLineLinkData = copy(sectionId = sectionId)
	override def withTextId(textId: Int): DocLineLinkData = copy(textId = textId)
	
	override def copyPlacedLink(parentId: Int, childId: Int, orderIndex: Int) =
		copy(sectionId = parentId, textId = childId, orderIndex = orderIndex)
}

