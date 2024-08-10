package vf.optidepy.database.access.many.library.doc.link

import utopia.flow.collection.immutable.IntSet
import utopia.flow.generic.casting.ValueConversions._
import utopia.vault.database.Connection
import utopia.vault.nosql.access.many.model.ManyRowModelAccess
import utopia.vault.nosql.view.ViewFactory
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.DocLineLinkDbFactory
import vf.optidepy.database.storable.library.DocLineLinkDbModel
import vf.optidepy.model.stored.library.DocLineLink

object ManyDocLineLinksAccess extends ViewFactory[ManyDocLineLinksAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): ManyDocLineLinksAccess = _ManyDocLineLinksAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _ManyDocLineLinksAccess(override val accessCondition: Option[Condition]) 
		extends ManyDocLineLinksAccess
}

/**
  * A common trait for access points which target multiple doc line links at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait ManyDocLineLinksAccess 
	extends ManyPlacedLinksAccessLike[DocLineLink, ManyDocLineLinksAccess] with ManyRowModelAccess[DocLineLink]
{
	// COMPUTED	--------------------
	
	/**
	  * section ids of the accessible doc line links
	  */
	def sectionIds(implicit connection: Connection) = parentIds
	/**
	  * text ids of the accessible doc line links
	  */
	def textIds(implicit connection: Connection) = childIds
	
	
	// IMPLEMENTED	--------------------
	
	override protected def self = this
	override def factory = DocLineLinkDbFactory
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	override protected def model = DocLineLinkDbModel
	
	override def apply(condition: Condition): ManyDocLineLinksAccess = ManyDocLineLinksAccess(condition)
	
	
	// OTHER	--------------------
	
	/**
	  * @param sectionId section id to target
	  * @return Copy of this access point that only includes doc line links with the specified section id
	  */
	def inSection(sectionId: Int) = filter(model.sectionId.column <=> sectionId)
	/**
	  * @param sectionIds Targeted section ids
	  * @return Copy of this access point that only includes doc line links where section id is within the specified
	  *  value set
	  */
	def inSections(sectionIds: Iterable[Int]) = filter(model.sectionId.column.in(IntSet.from(sectionIds)))
	
	/**
	  * @param textId text id to target
	  * @return Copy of this access point that only includes doc line links with the specified text id
	  */
	def toLine(textId: Int) = filter(model.textId.column <=> textId)
	/**
	  * @param textIds Targeted text ids
	  * @return Copy of this access point that only includes doc line links where text id is within the specified
	  *  value set
	  */
	def toLines(textIds: Iterable[Int]) = filter(model.textId.column.in(IntSet.from(textIds)))
}

