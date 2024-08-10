package vf.optidepy.database.access.many.library.doc.section

import utopia.vault.database.Connection
import utopia.vault.nosql.access.many.model.ManyRowModelAccess
import utopia.vault.nosql.view.ViewFactory
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.SubSectionDbFactory
import vf.optidepy.database.storable.library.SubSectionLinkDbModel
import vf.optidepy.model.combined.library.SubSection

object ManySubSectionsAccess extends ViewFactory[ManySubSectionsAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): ManySubSectionsAccess = _ManySubSectionsAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _ManySubSectionsAccess(override val accessCondition: Option[Condition]) 
		extends ManySubSectionsAccess
}

/**
  * A common trait for access points that return multiple sub sections at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024
  */
trait ManySubSectionsAccess 
	extends ManyDocSectionsAccessLike[SubSection, ManySubSectionsAccess] with ManyRowModelAccess[SubSection]
{
	// COMPUTED	--------------------
	
	/**
	  * parent ids of the accessible sub section links
	  */
	def linkParentIds(implicit connection: Connection) = 
		pullColumn(linkModel.parentId.column).map { v => v.getInt }
	
	/**
	  * child ids of the accessible sub section links
	  */
	def linkChildIds(implicit connection: Connection) = pullColumn(linkModel.childId.column)
		.map { v => v.getInt }
	
	/**
	  * order indices of the accessible sub section links
	  */
	def linkOrderIndices(implicit connection: Connection) = 
		pullColumn(linkModel.orderIndex.column).map { v => v.getInt }
	
	/**
	  * Model (factory) used for interacting the sub section links associated with this sub section
	  */
	protected def linkModel = SubSectionLinkDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = SubSectionDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): ManySubSectionsAccess = ManySubSectionsAccess(condition)
}

