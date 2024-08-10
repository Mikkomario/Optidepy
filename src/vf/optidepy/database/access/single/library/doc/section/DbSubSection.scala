package vf.optidepy.database.access.single.library.doc.section

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.UnconditionalView
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.SubSectionDbFactory
import vf.optidepy.database.storable.library.{DocSectionDbModel, SubSectionLinkDbModel}
import vf.optidepy.model.combined.library.SubSection

/**
  * Used for accessing individual sub sections
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbSubSection extends SingleRowModelAccess[SubSection] with UnconditionalView with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * A database model (factory) used for interacting with the linked link
	  */
	protected def linkModel = SubSectionLinkDbModel
	
	/**
	  * A database model (factory) used for interacting with linked sections
	  */
	private def model = DocSectionDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = SubSectionDbFactory
	
	
	// OTHER	--------------------
	
	/**
	  * @param id Database id of the targeted sub section
	  * @return An access point to that sub section
	  */
	def apply(id: Int) = DbSingleSubSection(id)
	
	/**
	  * @param condition Filter condition to apply in addition to this root view's condition. Should yield
	  *  unique sub sections.
	  * @return An access point to the sub section that satisfies the specified condition
	  */
	protected def distinct(condition: Condition) = UniqueSubSectionAccess(condition)
}

