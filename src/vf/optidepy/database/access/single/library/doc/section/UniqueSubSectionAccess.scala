package vf.optidepy.database.access.single.library.doc.section

import utopia.vault.database.Connection
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
	  * Id of the parent section, which contains the child section. 
	  * None if no sub section link (or value) was found.
	  */
	def linkParentId(implicit connection: Connection) = pullColumn(linkModel.parentId.column).int
	
	/**
	  * Id of the section contained within the parent section. 
	  * None if no sub section link (or value) was found.
	  */
	def linkChildId(implicit connection: Connection) = pullColumn(linkModel.childId.column).int
	
	/**
	  * 0-based index determining the location where the linked item is placed. 
	  * None if no sub section link (or value) was found.
	  */
	def linkOrderIndex(implicit connection: Connection) = pullColumn(linkModel.orderIndex.column).int
	
	/**
	  * A database model (factory) used for interacting with the linked link
	  */
	protected def linkModel = SubSectionLinkDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = SubSectionDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): UniqueSubSectionAccess = UniqueSubSectionAccess(condition)
}

