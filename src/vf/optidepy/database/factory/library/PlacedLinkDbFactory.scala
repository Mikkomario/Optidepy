package vf.optidepy.database.factory.library

import utopia.flow.generic.model.template.ModelLike.AnyModel
import utopia.vault.model.immutable.Table
import utopia.vault.sql.OrderBy
import vf.optidepy.database.props.library.PlacedLinkDbProps
import vf.optidepy.model.partial.library.PlacedLinkData
import vf.optidepy.model.stored.library.PlacedLink

object PlacedLinkDbFactory
{
	// OTHER	--------------------
	
	/**
	  * @param table Table from which data is read
	  * @param dbProps Database properties used when reading column data
	  * @return A factory used for parsing placed links from database model data
	  */
	def apply(table: Table, dbProps: PlacedLinkDbProps): PlacedLinkDbFactory = 
		_PlacedLinkDbFactory(table, dbProps)
	
	
	// NESTED	--------------------
	
	/**
	  * @param table Table from which data is read
	  * @param dbProps Database properties used when reading column data
	  */
	private case class _PlacedLinkDbFactory(table: Table, dbProps: PlacedLinkDbProps) extends PlacedLinkDbFactory
	{
		// IMPLEMENTED	--------------------
		
		override def defaultOrdering: Option[OrderBy] = None
		
		/**
		  * @param model Model from which additional data may be read
		  * @param id Id to assign to the read/parsed placed link
		  * @param parentId parent id to assign to the new placed link
		  * @param childId child id to assign to the new placed link
		  * @param orderIndex order index to assign to the new placed link
		  */
		override protected def apply(model: AnyModel, id: Int, parentId: Int, childId: Int, 
			orderIndex: Int) = 
			PlacedLink(id, PlacedLinkData(parentId, childId, orderIndex))
	}
}

/**
  * Common trait for factories which parse placed link data from database-originated models
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait PlacedLinkDbFactory extends PlacedLinkDbFactoryLike[PlacedLink]

