package vf.optidepy.database.access.many.library.doc.section

import utopia.vault.nosql.access.many.model.ManyRowModelAccess
import utopia.vault.nosql.view.ViewFactory
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.DocSectionDbFactory
import vf.optidepy.model.stored.library.DocSection

object ManyDocSectionsAccess extends ViewFactory[ManyDocSectionsAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): ManyDocSectionsAccess = _ManyDocSectionsAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _ManyDocSectionsAccess(override val accessCondition: Option[Condition]) 
		extends ManyDocSectionsAccess
}

/**
  * A common trait for access points which target multiple doc sections at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait ManyDocSectionsAccess 
	extends ManyDocSectionsAccessLike[DocSection, ManyDocSectionsAccess] with ManyRowModelAccess[DocSection]
{
	// IMPLEMENTED	--------------------
	
	override def factory = DocSectionDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): ManyDocSectionsAccess = ManyDocSectionsAccess(condition)
}

