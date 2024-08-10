package vf.optidepy.database.access.single.library.doc.link

import utopia.vault.nosql.view.ViewFactory
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.SubSectionLinkDbFactory
import vf.optidepy.database.storable.library.SubSectionLinkDbModel
import vf.optidepy.model.stored.library.SubSectionLink

object UniqueSubSectionLinkAccess extends ViewFactory[UniqueSubSectionLinkAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): UniqueSubSectionLinkAccess = 
		_UniqueSubSectionLinkAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _UniqueSubSectionLinkAccess(override val accessCondition: Option[Condition]) 
		extends UniqueSubSectionLinkAccess
}

/**
  * A common trait for access points that return individual and distinct sub section links.
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait UniqueSubSectionLinkAccess 
	extends UniquePlacedLinkAccessLike[SubSectionLink, UniqueSubSectionLinkAccess]
{
	// IMPLEMENTED	--------------------
	
	override def factory = SubSectionLinkDbFactory
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	override protected def model = SubSectionLinkDbModel
	
	override protected def self = this
	
	override
		 def apply(condition: Condition): UniqueSubSectionLinkAccess = UniqueSubSectionLinkAccess(condition)
}

