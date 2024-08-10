package vf.optidepy.model.partial.library

import utopia.flow.collection.immutable.Single
import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.factory.FromModelFactoryWithSchema
import utopia.flow.generic.model.immutable.{Model, ModelDeclaration, PropertyDeclaration}
import utopia.flow.generic.model.mutable.DataType.IntType

object PlacedLinkData extends FromModelFactoryWithSchema[PlacedLinkData]
{
	// ATTRIBUTES	--------------------
	
	override lazy val schema = 
		ModelDeclaration(Vector(PropertyDeclaration("parentId", IntType, Single("parent_id")), 
			PropertyDeclaration("childId", IntType, Single("child_id")), PropertyDeclaration("orderIndex", 
			IntType, Single("order_index"), 0)))
	
	
	// IMPLEMENTED	--------------------
	
	override protected def fromValidatedModel(valid: Model) = 
		PlacedLinkData(valid("parentId").getInt, valid("childId").getInt, valid("orderIndex").getInt)
	
	
	// OTHER	--------------------
	
	/**
	  * Creates a new placed link
	  * @param parentId Id of the element within which the linked item is placed
	  * @param childId Id of the linked / placed element
	  * @param orderIndex 0-based index determining the location where the linked item is placed
	  * @return placed link with the specified properties
	  */
	def apply(parentId: Int, childId: Int, orderIndex: Int = 0): PlacedLinkData = 
		_PlacedLinkData(parentId, childId, orderIndex)
	
	
	// NESTED	--------------------
	
	/**
	  * Concrete implementation of the placed link data trait
	  * @param parentId Id of the element within which the linked item is placed
	  * @param childId Id of the linked / placed element
	  * @param orderIndex 0-based index determining the location where the linked item is placed
	  * @author Mikko Hilpinen
	  * @since 09.08.2024
	  */
	private case class _PlacedLinkData(parentId: Int, childId: Int, 
		orderIndex: Int = 0) extends PlacedLinkData
	{
		// IMPLEMENTED	--------------------
		
		/**
		  * @param parentId Id of the element within which the linked item is placed
		  * @param childId Id of the linked / placed element
		  * @param orderIndex 0-based index determining the location where the linked item is placed
		  */
		override def copyPlacedLink(parentId: Int, childId: Int, orderIndex: Int = 0) = 
			_PlacedLinkData(parentId, childId, orderIndex)
	}
}

/**
  * Common trait for links which place the referenced item to a specific location within the parent item
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait PlacedLinkData extends PlacedLinkDataLike[PlacedLinkData]

