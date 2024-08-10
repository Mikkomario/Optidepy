package vf.optidepy.database.access.single.library.doc.text

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.view.ViewFactory
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.DocTextDbFactory
import vf.optidepy.model.stored.library.DocText

object UniqueDocTextAccess extends ViewFactory[UniqueDocTextAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): UniqueDocTextAccess = _UniqueDocTextAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _UniqueDocTextAccess(override val accessCondition: Option[Condition]) 
		extends UniqueDocTextAccess
}

/**
  * A common trait for access points that return individual and distinct doc texts.
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait UniqueDocTextAccess 
	extends UniqueDocTextAccessLike[DocText, UniqueDocTextAccess] with SingleRowModelAccess[DocText]
{
	// IMPLEMENTED	--------------------
	
	override def factory = DocTextDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): UniqueDocTextAccess = UniqueDocTextAccess(condition)
}

