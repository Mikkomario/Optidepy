package vf.optidepy.database.access.single.library.doc.section

import utopia.vault.database.Connection
import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.view.ViewFactory
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.DocSectionWithHeaderDbFactory
import vf.optidepy.database.storable.library.DocTextDbModel
import vf.optidepy.model.combined.library.DocSectionWithHeader

object UniqueDocSectionWithHeaderAccess extends ViewFactory[UniqueDocSectionWithHeaderAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): UniqueDocSectionWithHeaderAccess = 
		_UniqueDocSectionWithHeaderAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _UniqueDocSectionWithHeaderAccess(override val accessCondition: Option[Condition]) 
		extends UniqueDocSectionWithHeaderAccess
}

/**
  * A common trait for access points that return distinct doc section with headers
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait UniqueDocSectionWithHeaderAccess 
	extends UniqueDocSectionAccessLike[DocSectionWithHeader, UniqueDocSectionWithHeaderAccess] 
		with SingleRowModelAccess[DocSectionWithHeader]
{
	// COMPUTED	--------------------
	
	/**
	  * Wrapped text contents. 
	  * None if no doc text (or value) was found.
	  */
	def headerText(implicit connection: Connection) = pullColumn(headerModel.text.column).getString
	
	/**
	  * Level of indentation applicable to this line / text. 
	  * None if no doc text (or value) was found.
	  */
	def headerIndentation(implicit connection: Connection) = pullColumn(headerModel.indentation.column).int
	
	/**
	  * Time when this text was first introduced. 
	  * None if no doc text (or value) was found.
	  */
	def headerCreated(implicit connection: Connection) = pullColumn(headerModel.created.column).instant
	
	/**
	  * A database model (factory) used for interacting with the linked header
	  */
	protected def headerModel = DocTextDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = DocSectionWithHeaderDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): UniqueDocSectionWithHeaderAccess = 
		UniqueDocSectionWithHeaderAccess(condition)
}

