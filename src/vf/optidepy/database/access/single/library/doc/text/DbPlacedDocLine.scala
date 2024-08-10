package vf.optidepy.database.access.single.library.doc.text

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.UnconditionalView
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.PlacedDocLineDbFactory
import vf.optidepy.database.storable.library.{DocLineLinkDbModel, DocTextDbModel}
import vf.optidepy.model.combined.library.PlacedDocLine

/**
  * Used for accessing individual placed doc lines
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbPlacedDocLine extends SingleRowModelAccess[PlacedDocLine] with UnconditionalView with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * A database model (factory) used for interacting with the linked link
	  */
	protected def linkModel = DocLineLinkDbModel
	
	/**
	  * A database model (factory) used for interacting with linked doc texts
	  */
	private def model = DocTextDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = PlacedDocLineDbFactory
	
	
	// OTHER	--------------------
	
	/**
	  * @param id Database id of the targeted placed doc line
	  * @return An access point to that placed doc line
	  */
	def apply(id: Int) = DbSinglePlacedDocLine(id)
	
	/**
	  * @param condition Filter condition to apply in addition to this root view's condition. Should yield
	  *  unique placed doc lines.
	  * @return An access point to the placed doc line that satisfies the specified condition
	  */
	protected def distinct(condition: Condition) = UniquePlacedDocLineAccess(condition)
}

