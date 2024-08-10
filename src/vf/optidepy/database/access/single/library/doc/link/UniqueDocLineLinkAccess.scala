package vf.optidepy.database.access.single.library.doc.link

import utopia.vault.database.Connection
import utopia.vault.nosql.view.ViewFactory
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.DocLineLinkDbFactory
import vf.optidepy.database.storable.library.DocLineLinkDbModel
import vf.optidepy.model.stored.library.DocLineLink

object UniqueDocLineLinkAccess extends ViewFactory[UniqueDocLineLinkAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override
		 def apply(condition: Condition): UniqueDocLineLinkAccess = _UniqueDocLineLinkAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _UniqueDocLineLinkAccess(override val accessCondition: Option[Condition]) 
		extends UniqueDocLineLinkAccess
}

/**
  * A common trait for access points that return individual and distinct doc line links.
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait UniqueDocLineLinkAccess extends UniquePlacedLinkAccessLike[DocLineLink, UniqueDocLineLinkAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * Id of the doc section linked with this doc line link. 
	  * None if no doc line link (or value) was found.
	  */
	def sectionId(implicit connection: Connection) = parentId
	
	/**
	  * Id of the doc text linked with this doc line link. 
	  * None if no doc line link (or value) was found.
	  */
	def textId(implicit connection: Connection) = childId
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = DocLineLinkDbFactory
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	override protected def model = DocLineLinkDbModel
	
	override protected def self = this
	
	override def apply(condition: Condition): UniqueDocLineLinkAccess = UniqueDocLineLinkAccess(condition)
}

