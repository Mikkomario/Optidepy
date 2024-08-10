package vf.optidepy.model.partial.library

import utopia.flow.collection.immutable.Single
import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.factory.FromModelFactoryWithSchema
import utopia.flow.generic.model.immutable.{Model, ModelDeclaration, PropertyDeclaration}
import utopia.flow.generic.model.mutable.DataType.IntType
import utopia.flow.generic.model.template.ModelConvertible
import vf.optidepy.model.factory.library.SubSectionLinkFactory

object SubSectionLinkData extends FromModelFactoryWithSchema[SubSectionLinkData]
{
	// ATTRIBUTES	--------------------
	
	override lazy val schema = 
		ModelDeclaration(Vector(PropertyDeclaration("parentId", IntType, Single("parent_id")), 
			PropertyDeclaration("childId", IntType, Single("child_id")), PropertyDeclaration("orderIndex", 
			IntType, Single("order_index"), 0)))
	
	
	// IMPLEMENTED	--------------------
	
	override protected def fromValidatedModel(valid: Model) = 
		SubSectionLinkData(valid("parentId").getInt, valid("childId").getInt, valid("orderIndex").getInt)
}

/**
  * Represents a link between two doc sections, assigning one as the subsection of another
  * @param parentId Id of the parent section, which contains the child section
  * @param childId Id of the section contained within the parent section
  * @param orderIndex 0-based index determining the location where the linked item is placed
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class SubSectionLinkData(parentId: Int, childId: Int, orderIndex: Int = 0) 
	extends ModelConvertible with PlacedLinkData with PlacedLinkDataLike[SubSectionLinkData]
		with SubSectionLinkFactory[SubSectionLinkData]
{
	// IMPLEMENTED	--------------------
	
	override def copyPlacedLink(parentId: Int, childId: Int, orderIndex: Int) = 
		copy(parentId = parentId, childId = childId, orderIndex = orderIndex)
}

