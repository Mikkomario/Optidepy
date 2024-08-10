package vf.optidepy.database.access.many.library.doc.text

import utopia.vault.nosql.access.many.model.ManyRowModelAccess
import utopia.vault.nosql.view.ViewFactory
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.DocTextDbFactory
import vf.optidepy.model.stored.library.DocText

object ManyDocTextsAccess extends ViewFactory[ManyDocTextsAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): ManyDocTextsAccess = _ManyDocTextsAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _ManyDocTextsAccess(override val accessCondition: Option[Condition]) 
		extends ManyDocTextsAccess
}

/**
  * A common trait for access points which target multiple doc texts at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait ManyDocTextsAccess 
	extends ManyDocTextsAccessLike[DocText, ManyDocTextsAccess] with ManyRowModelAccess[DocText]
{
	// IMPLEMENTED	--------------------
	
	override def factory = DocTextDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): ManyDocTextsAccess = ManyDocTextsAccess(condition)
}

