package vf.optidepy.database.access.single.library.doc.section

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.view.ViewFactory
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.DocSectionDbFactory
import vf.optidepy.model.stored.library.DocSection

object UniqueDocSectionAccess extends ViewFactory[UniqueDocSectionAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override
		 def apply(condition: Condition): UniqueDocSectionAccess = _UniqueDocSectionAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _UniqueDocSectionAccess(override val accessCondition: Option[Condition]) 
		extends UniqueDocSectionAccess
}

/**
  * A common trait for access points that return individual and distinct doc sections.
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait UniqueDocSectionAccess 
	extends UniqueDocSectionAccessLike[DocSection, UniqueDocSectionAccess] 
		with SingleRowModelAccess[DocSection]
{
	// IMPLEMENTED	--------------------
	
	override def factory = DocSectionDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): UniqueDocSectionAccess = UniqueDocSectionAccess(condition)
}

