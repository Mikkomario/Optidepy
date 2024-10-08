package vf.optidepy.database.access.single.library.doc.text

import utopia.vault.database.Connection
import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.view.ViewFactory
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.PlacedDocLineDbFactory
import vf.optidepy.database.storable.library.DocLineLinkDbModel
import vf.optidepy.model.combined.library.PlacedDocLine

object UniquePlacedDocLineAccess extends ViewFactory[UniquePlacedDocLineAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): UniquePlacedDocLineAccess = 
		_UniquePlacedDocLineAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _UniquePlacedDocLineAccess(override val accessCondition: Option[Condition]) 
		extends UniquePlacedDocLineAccess
}

/**
  * A common trait for access points that return distinct placed doc lines
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait UniquePlacedDocLineAccess 
	extends UniqueDocTextAccessLike[PlacedDocLine, UniquePlacedDocLineAccess] 
		with SingleRowModelAccess[PlacedDocLine]
{
	// COMPUTED	--------------------
	
	/**
	  * Id of the doc section linked with this doc line link. 
	  * None if no doc line link (or value) was found.
	  */
	def linkSectionId(implicit connection: Connection) = pullColumn(linkModel.sectionId.column).int
	
	/**
	  * Id of the doc text linked with this doc line link. 
	  * None if no doc line link (or value) was found.
	  */
	def linkTextId(implicit connection: Connection) = pullColumn(linkModel.textId.column).int
	
	/**
	  * 0-based index determining the location where the linked item is placed. 
	  * None if no doc line link (or value) was found.
	  */
	def linkOrderIndex(implicit connection: Connection) = pullColumn(linkModel.orderIndex.column).int
	
	/**
	  * A database model (factory) used for interacting with the linked link
	  */
	protected def linkModel = DocLineLinkDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = PlacedDocLineDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): UniquePlacedDocLineAccess = UniquePlacedDocLineAccess(condition)
}

