package vf.optidepy.database.access.single.library.doc.link

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.UnconditionalView
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.SubSectionLinkDbFactory
import vf.optidepy.database.storable.library.SubSectionLinkDbModel
import vf.optidepy.model.stored.library.SubSectionLink

/**
  * Used for accessing individual sub section links
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbSubSectionLink extends SingleRowModelAccess[SubSectionLink] with UnconditionalView with Indexed
{
	// IMPLEMENTED	--------------------
	
	override def factory = SubSectionLinkDbFactory
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	private def model = SubSectionLinkDbModel
	
	
	// OTHER	--------------------
	
	/**
	  * @param id Database id of the targeted sub section link
	  * @return An access point to that sub section link
	  */
	def apply(id: Int) = DbSingleSubSectionLink(id)
	
	/**
	  * @param condition Filter condition to apply in addition to this root view's condition. Should yield unique sub
	  *  section links.
	  * @return An access point to the sub section link that satisfies the specified condition
	  */
	protected def distinct(condition: Condition) = UniqueSubSectionLinkAccess(condition)
}

