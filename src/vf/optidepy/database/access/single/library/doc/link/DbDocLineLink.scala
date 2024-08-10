package vf.optidepy.database.access.single.library.doc.link

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.UnconditionalView
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.DocLineLinkDbFactory
import vf.optidepy.database.storable.library.DocLineLinkDbModel
import vf.optidepy.model.stored.library.DocLineLink

/**
  * Used for accessing individual doc line links
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbDocLineLink extends SingleRowModelAccess[DocLineLink] with UnconditionalView with Indexed
{
	// IMPLEMENTED	--------------------
	
	override def factory = DocLineLinkDbFactory
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	private def model = DocLineLinkDbModel
	
	
	// OTHER	--------------------
	
	/**
	  * @param id Database id of the targeted doc line link
	  * @return An access point to that doc line link
	  */
	def apply(id: Int) = DbSingleDocLineLink(id)
	
	/**
	  * 
		@param condition Filter condition to apply in addition to this root view's condition. Should yield unique doc
	  *  line links.
	  * @return An access point to the doc line link that satisfies the specified condition
	  */
	protected def distinct(condition: Condition) = UniqueDocLineLinkAccess(condition)
}

