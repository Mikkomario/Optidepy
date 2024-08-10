package vf.optidepy.database.access.many.library.doc.link

import utopia.flow.collection.immutable.IntSet
import utopia.flow.generic.casting.ValueConversions._
import utopia.vault.nosql.access.many.model.ManyRowModelAccess
import utopia.vault.nosql.view.ViewFactory
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.SubSectionLinkDbFactory
import vf.optidepy.database.storable.library.SubSectionLinkDbModel
import vf.optidepy.model.stored.library.SubSectionLink

object ManySubSectionLinksAccess extends ViewFactory[ManySubSectionLinksAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): ManySubSectionLinksAccess = 
		_ManySubSectionLinksAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _ManySubSectionLinksAccess(override val accessCondition: Option[Condition]) 
		extends ManySubSectionLinksAccess
}

/**
  * A common trait for access points which target multiple sub section links at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait ManySubSectionLinksAccess 
	extends ManyPlacedLinksAccessLike[SubSectionLink, ManySubSectionLinksAccess] with ManyRowModelAccess[SubSectionLink]
{
	// IMPLEMENTED	--------------------
	
	override protected def self = this
	override def factory = SubSectionLinkDbFactory
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	override protected def model = SubSectionLinkDbModel
	
	override def apply(condition: Condition): ManySubSectionLinksAccess = ManySubSectionLinksAccess(condition)
	
	
	// OTHER	--------------------
	
	/**
	  * @param parentId parent id to target
	  * @return Copy of this access point that only includes sub section links with the specified parent id
	  */
	def inSection(parentId: Int) = filter(model.parentId.column <=> parentId)
	/**
	  * @param parentIds Targeted parent ids
	  * @return Copy of this access point that only includes sub section links where parent id is within the specified
	  *  value set
	  */
	def inSections(parentIds: Iterable[Int]) =
		filter(model.parentId.column.in(IntSet.from(parentIds)))
	
	/**
	  * @param childId child id to target
	  * @return Copy of this access point that only includes sub section links with the specified child id
	  */
	def toSubSection(childId: Int) = filter(model.childId.column <=> childId)
	/**
	  * @param childIds Targeted child ids
	  * @return Copy of this access point that only includes sub section links where child id is within the specified
	  *  value set
	  */
	def toSubSections(childIds: Iterable[Int]) =
		filter(model.childId.column.in(IntSet.from(childIds)))
}

