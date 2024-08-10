package vf.optidepy.database.storable.library

import utopia.flow.generic.model.immutable.Value
import utopia.vault.model.immutable.Table
import vf.optidepy.database.props.library.{PlacedLinkDbProps, PlacedLinkDbPropsWrapper}
import vf.optidepy.model.partial.library.PlacedLinkData
import vf.optidepy.model.stored.library.PlacedLink

object PlacedLinkDbModelFactory
{
	// OTHER	--------------------
	
	/**
	  * @return A factory for constructing placed link database models
	  */
	def apply(table: Table, dbProps: PlacedLinkDbProps) = PlacedLinkDbModelFactoryImpl(table, dbProps)
	
	
	// NESTED	--------------------
	
	/**
	  * Used for constructing PlacedLinkDbModel instances and for inserting placed links to the database
	  * @param table Table targeted by these models
	  * @param placedLinkDbProps Properties which specify how the database interactions are performed
	  * @author Mikko Hilpinen
	  * @since 09.08.2024, v1.2
	  */
	case class PlacedLinkDbModelFactoryImpl(table: Table, placedLinkDbProps: PlacedLinkDbProps) 
		extends PlacedLinkDbModelFactory with PlacedLinkDbPropsWrapper
	{
		// IMPLEMENTED	--------------------
		
		override def apply(data: PlacedLinkData) = 
			apply(None, Some(data.parentId), Some(data.childId), Some(data.orderIndex))
		
		/**
		  * @param childId Id of the linked / placed element
		  * @return A model containing only the specified child id
		  */
		override def withChildId(childId: Int) = apply(childId = Some(childId))
		
		override def withId(id: Int) = apply(id = Some(id))
		
		/**
		  * @param orderIndex 0-based index determining the location where the linked item is placed
		  * @return A model containing only the specified order index
		  */
		override def withOrderIndex(orderIndex: Int) = apply(orderIndex = Some(orderIndex))
		
		/**
		  * @param parentId Id of the element within which the linked item is placed
		  * @return A model containing only the specified parent id
		  */
		override def withParentId(parentId: Int) = apply(parentId = Some(parentId))
		
		override protected def complete(id: Value, data: PlacedLinkData) = PlacedLink(id.getInt, data)
		
		
		// OTHER	--------------------
		
		/**
		  * @param id placed link database id
		  * @return Constructs a new placed link database model with the specified properties
		  */
		def apply(id: Option[Int] = None, parentId: Option[Int] = None, childId: Option[Int] = None, 
			orderIndex: Option[Int] = None): PlacedLinkDbModel = 
			_PlacedLinkDbModel(table, placedLinkDbProps, id, parentId, childId, orderIndex)
	}
	
	/**
	  * Used for interacting with PlacedLinks in the database
	  * @param table Table interacted with when using this model
	  * @param dbProps Configurations of the interacted database properties
	  * @param id placed link database id
	  * @author Mikko Hilpinen
	  * @since 09.08.2024, v1.2
	  */
	private case class _PlacedLinkDbModel(table: Table, dbProps: PlacedLinkDbProps, id: Option[Int] = None, 
		parentId: Option[Int] = None, childId: Option[Int] = None, orderIndex: Option[Int] = None) 
		extends PlacedLinkDbModel
	{
		// IMPLEMENTED	--------------------
		
		/**
		  * @param id Id to assign to the new model (default = currently assigned id)
		  * @param parentId parent id to assign to the new model (default = currently assigned value)
		  * @param childId child id to assign to the new model (default = currently assigned value)
		  * @param orderIndex order index to assign to the new model (default = currently assigned value)
		  * @return Copy of this model with the specified placed link properties
		  */
		override protected def copyPlacedLink(id: Option[Int] = id, parentId: Option[Int] = parentId, 
			childId: Option[Int] = childId, orderIndex: Option[Int] = orderIndex) =
			copy(id = id, parentId = parentId, childId = childId, orderIndex = orderIndex)
	}
}

/**
  * Common trait for factories yielding placed link database models
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait PlacedLinkDbModelFactory 
	extends PlacedLinkDbModelFactoryLike[PlacedLinkDbModel, PlacedLink, PlacedLinkData]

