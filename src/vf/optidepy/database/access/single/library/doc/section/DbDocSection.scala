package vf.optidepy.database.access.single.library.doc.section

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.UnconditionalView
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.DocSectionDbFactory
import vf.optidepy.database.storable.library.DocSectionDbModel
import vf.optidepy.model.stored.library.DocSection

/**
  * Used for accessing individual doc sections
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbDocSection extends SingleRowModelAccess[DocSection] with UnconditionalView with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	protected def model = DocSectionDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = DocSectionDbFactory
	
	
	// OTHER	--------------------
	
	/**
	  * @param id Database id of the targeted doc section
	  * @return An access point to that doc section
	  */
	def apply(id: Int) = DbSingleDocSection(id)
	
	/**
	  * @param condition Filter condition to apply in addition to this root view's condition. Should yield
	  *  unique doc sections.
	  * @return An access point to the doc section that satisfies the specified condition
	  */
	protected def distinct(condition: Condition) = UniqueDocSectionAccess(condition)
}

