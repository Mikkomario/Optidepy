package vf.optidepy.database.storable.library

import utopia.flow.generic.model.immutable.Value
import utopia.vault.model.immutable.DbPropertyDeclaration
import vf.optidepy.database.OptidepyTables
import vf.optidepy.database.props.library.PlacedLinkDbProps
import vf.optidepy.model.factory.library.DocLineLinkFactory
import vf.optidepy.model.partial.library.DocLineLinkData
import vf.optidepy.model.stored.library.DocLineLink

/**
  * Used for constructing DocLineLinkDbModel instances and for inserting doc line links to the database
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DocLineLinkDbModel 
	extends PlacedLinkDbModelFactoryLike[DocLineLinkDbModel, DocLineLink, DocLineLinkData] 
		with DocLineLinkFactory[DocLineLinkDbModel] with PlacedLinkDbProps
{
	// ATTRIBUTES	--------------------
	
	override lazy val id = DbPropertyDeclaration("id", index)
	
	/**
	  * Database property used for interacting with section ids
	  */
	lazy val sectionId = property("sectionId")
	
	/**
	  * Database property used for interacting with text ids
	  */
	lazy val textId = property("textId")
	
	/**
	  * Database property used for interacting with order indices
	  */
	override lazy val orderIndex = property("orderIndex")
	
	
	// IMPLEMENTED	--------------------
	
	override def childId = textId
	
	override def parentId = sectionId
	
	override def table = OptidepyTables.docLineLink
	
	override def apply(data: DocLineLinkData) = 
		apply(None, Some(data.sectionId), Some(data.textId), Some(data.orderIndex))
	
	override def withId(id: Int) = apply(id = Some(id))
	
	/**
	  * @param orderIndex 0-based index determining the location where the linked item is placed
	  * @return A model containing only the specified order index
	  */
	override def withOrderIndex(orderIndex: Int) = apply(orderIndex = Some(orderIndex))
	
	/**
	  * @param sectionId Id of the doc section linked with this doc line link
	  * @return A model containing only the specified section id
	  */
	override def withSectionId(sectionId: Int) = apply(sectionId = Some(sectionId))
	
	/**
	  * @param textId Id of the doc text linked with this doc line link
	  * @return A model containing only the specified text id
	  */
	override def withTextId(textId: Int) = apply(textId = Some(textId))
	
	override protected def complete(id: Value, data: DocLineLinkData) = DocLineLink(id.getInt, data)
}

/**
  * Used for interacting with DocLineLinks in the database
  * @param id doc line link database id
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DocLineLinkDbModel(id: Option[Int] = None, sectionId: Option[Int] = None, 
	textId: Option[Int] = None, orderIndex: Option[Int] = None) 
	extends PlacedLinkDbModel with PlacedLinkDbModelLike[DocLineLinkDbModel] 
		with DocLineLinkFactory[DocLineLinkDbModel]
{
	// IMPLEMENTED	--------------------
	
	override def childId = textId
	
	override def dbProps = DocLineLinkDbModel
	
	override def parentId = sectionId
	
	override def table = DocLineLinkDbModel.table
	
	/**
	  * @param id Id to assign to the new model (default = currently assigned id)
	  * @param parentId parent id to assign to the new model (default = currently assigned value)
	  * @param childId child id to assign to the new model (default = currently assigned value)
	  * @param orderIndex order index to assign to the new model (default = currently assigned value)
	  */
	override def copyPlacedLink(id: Option[Int] = id, parentId: Option[Int] = parentId, 
		childId: Option[Int] = childId, orderIndex: Option[Int] = orderIndex) = 
		copy(id = id, sectionId = parentId, textId = childId, orderIndex = orderIndex)
	
	/**
	  * @param sectionId Id of the doc section linked with this doc line link
	  * @return A new copy of this model with the specified section id
	  */
	override def withSectionId(sectionId: Int) = copy(sectionId = Some(sectionId))
	
	/**
	  * @param textId Id of the doc text linked with this doc line link
	  * @return A new copy of this model with the specified text id
	  */
	override def withTextId(textId: Int) = copy(textId = Some(textId))
}

