package vf.optidepy.database.access.single.library.doc.section

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.view.ViewFactory
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.SubSectionDbFactory
import vf.optidepy.database.storable.library.SubSectionLinkDbModel
import vf.optidepy.model.combined.library.SubSection

object UniqueSubSectionAccess extends ViewFactory[UniqueSubSectionAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override
		 def apply(condition: Condition): UniqueSubSectionAccess = _UniqueSubSectionAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _UniqueSubSectionAccess(override val accessCondition: Option[Condition]) 
		extends UniqueSubSectionAccess
}

/**
  * A common trait for access points that return distinct sub sections
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait UniqueSubSectionAccess 
	extends UniqueDocSectionAccessLike[SubSection, UniqueSubSectionAccess] 
		with SingleRowModelAccess[SubSection]
{
	// COMPUTED	--------------------
	
	/**
	  * A database model (factory) used for interacting with the linked link
	  */
	protected def linkModel = SubSectionLinkDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = SubSectionDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): UniqueSubSectionAccess = UniqueSubSectionAccess(condition)
}

