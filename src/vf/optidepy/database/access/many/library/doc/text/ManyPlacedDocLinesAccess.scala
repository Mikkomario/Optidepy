package vf.optidepy.database.access.many.library.doc.text

import utopia.vault.database.Connection
import utopia.vault.nosql.access.many.model.ManyRowModelAccess
import utopia.vault.nosql.view.ViewFactory
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.PlacedDocLineDbFactory
import vf.optidepy.database.storable.library.DocLineLinkDbModel
import vf.optidepy.model.combined.library.PlacedDocLine

object ManyPlacedDocLinesAccess extends ViewFactory[ManyPlacedDocLinesAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): ManyPlacedDocLinesAccess = 
		_ManyPlacedDocLinesAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _ManyPlacedDocLinesAccess(override val accessCondition: Option[Condition]) 
		extends ManyPlacedDocLinesAccess
}

/**
  * A common trait for access points that return multiple placed doc lines at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024
  */
trait ManyPlacedDocLinesAccess 
	extends ManyDocTextsAccessLike[PlacedDocLine, ManyPlacedDocLinesAccess] 
		with ManyRowModelAccess[PlacedDocLine]
{
	// COMPUTED	--------------------
	
	/**
	  * section ids of the accessible doc line links
	  */
	def linkSectionIds(implicit connection: Connection) = 
		pullColumn(linkModel.sectionId.column).map { v => v.getInt }
	
	/**
	  * text ids of the accessible doc line links
	  */
	def linkTextIds(implicit connection: Connection) = pullColumn(linkModel.textId.column)
		.map { v => v.getInt }
	
	/**
	  * order indices of the accessible doc line links
	  */
	def linkOrderIndices(implicit connection: Connection) = 
		pullColumn(linkModel.orderIndex.column).map { v => v.getInt }
	
	/**
	  * Model (factory) used for interacting the doc line links associated with this placed doc line
	  */
	protected def linkModel = DocLineLinkDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = PlacedDocLineDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): ManyPlacedDocLinesAccess = ManyPlacedDocLinesAccess(condition)
}

