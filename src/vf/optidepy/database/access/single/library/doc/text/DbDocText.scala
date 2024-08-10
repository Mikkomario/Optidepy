package vf.optidepy.database.access.single.library.doc.text

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.UnconditionalView
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.DocTextDbFactory
import vf.optidepy.database.storable.library.DocTextDbModel
import vf.optidepy.model.stored.library.DocText

/**
  * Used for accessing individual doc texts
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbDocText extends SingleRowModelAccess[DocText] with UnconditionalView with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	protected def model = DocTextDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = DocTextDbFactory
	
	
	// OTHER	--------------------
	
	/**
	  * @param id Database id of the targeted doc text
	  * @return An access point to that doc text
	  */
	def apply(id: Int) = DbSingleDocText(id)
	
	/**
	  * @param condition Filter condition to apply in addition to this root view's condition. Should yield
	  *  unique doc texts.
	  * @return An access point to the doc text that satisfies the specified condition
	  */
	protected def distinct(condition: Condition) = UniqueDocTextAccess(condition)
}

