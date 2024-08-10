package vf.optidepy.database.storable.library

import utopia.flow.generic.casting.ValueConversions._
import utopia.vault.model.immutable.Storable
import utopia.vault.model.template.{FromIdFactory, HasId}
import vf.optidepy.database.props.library.PlacedLinkDbProps
import vf.optidepy.model.factory.library.PlacedLinkFactory

/**
  * Common trait for database models used for interacting with placed link data in the database
  * @tparam Repr Type of this DB model
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait PlacedLinkDbModelLike[+Repr] 
	extends Storable with HasId[Option[Int]] with FromIdFactory[Int, Repr] with PlacedLinkFactory[Repr]
{
	// ABSTRACT	--------------------
	
	def parentId: Option[Int]
	
	def childId: Option[Int]
	
	def orderIndex: Option[Int]
	
	/**
	  * Access to the database properties which are utilized in this model
	  */
	def dbProps: PlacedLinkDbProps
	
	/**
	  * @param id Id to assign to the new model (default = currently assigned id)
	  * @param parentId parent id to assign to the new model (default = currently assigned value)
	  * @param childId child id to assign to the new model (default = currently assigned value)
	  * @param orderIndex order index to assign to the new model (default = currently assigned value)
	  * @return Copy of this model with the specified placed link properties
	  */
	protected def copyPlacedLink(id: Option[Int] = id, parentId: Option[Int] = parentId, 
		childId: Option[Int] = childId, orderIndex: Option[Int] = orderIndex): Repr
	
	
	// IMPLEMENTED	--------------------
	
	override def valueProperties = 
		Vector(dbProps.id.name -> id, dbProps.parentId.name -> parentId, dbProps.childId.name -> childId, 
			dbProps.orderIndex.name -> orderIndex)
	
	/**
	  * @param childId Id of the linked / placed element
	  * @return A new copy of this model with the specified child id
	  */
	override def withChildId(childId: Int) = copyPlacedLink(childId = Some(childId))
	
	override def withId(id: Int) = copyPlacedLink(id = Some(id))
	
	/**
	  * @param orderIndex 0-based index determining the location where the linked item is placed
	  * @return A new copy of this model with the specified order index
	  */
	override def withOrderIndex(orderIndex: Int) = copyPlacedLink(orderIndex = Some(orderIndex))
	
	/**
	  * @param parentId Id of the element within which the linked item is placed
	  * @return A new copy of this model with the specified parent id
	  */
	override def withParentId(parentId: Int) = copyPlacedLink(parentId = Some(parentId))
}

