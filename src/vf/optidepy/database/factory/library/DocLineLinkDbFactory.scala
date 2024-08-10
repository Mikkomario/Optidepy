package vf.optidepy.database.factory.library

import utopia.flow.generic.model.template.ModelLike.AnyModel
import utopia.vault.sql.OrderBy
import vf.optidepy.database.storable.library.DocLineLinkDbModel
import vf.optidepy.model.partial.library.DocLineLinkData
import vf.optidepy.model.stored.library.DocLineLink

/**
  * Used for reading doc line link data from the DB
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DocLineLinkDbFactory extends PlacedLinkDbFactoryLike[DocLineLink]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * Model that specifies the how data is read
	  */
	override def dbProps = DocLineLinkDbModel
	
	override def defaultOrdering: Option[OrderBy] = None
	
	override def table = dbProps.table
	
	/**
	  * @param model Model from which additional data may be read
	  * @param id Id to assign to the read/parsed placed link
	  * @param parentId parent id to assign to the new placed link
	  * @param childId child id to assign to the new placed link
	  * @param orderIndex order index to assign to the new placed link
	  */
	override protected def apply(model: AnyModel, id: Int, parentId: Int, childId: Int, orderIndex: Int) = 
		DocLineLink(id, DocLineLinkData(parentId, childId, orderIndex))
}

