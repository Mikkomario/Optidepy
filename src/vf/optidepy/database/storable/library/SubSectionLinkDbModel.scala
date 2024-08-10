package vf.optidepy.database.storable.library

import utopia.flow.generic.model.immutable.Value
import utopia.vault.model.immutable.DbPropertyDeclaration
import vf.optidepy.database.OptidepyTables
import vf.optidepy.database.props.library.PlacedLinkDbProps
import vf.optidepy.model.factory.library.SubSectionLinkFactory
import vf.optidepy.model.partial.library.SubSectionLinkData
import vf.optidepy.model.stored.library.SubSectionLink

/**
  * Used for constructing SubSectionLinkDbModel instances and for inserting sub section links to the database
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object SubSectionLinkDbModel 
	extends PlacedLinkDbModelFactoryLike[SubSectionLinkDbModel, SubSectionLink, SubSectionLinkData] 
		with SubSectionLinkFactory[SubSectionLinkDbModel] with PlacedLinkDbProps
{
	// ATTRIBUTES	--------------------
	
	override lazy val id = DbPropertyDeclaration("id", index)
	
	/**
	  * Database property used for interacting with parent ids
	  */
	override lazy val parentId = property("parentId")
	
	/**
	  * Database property used for interacting with child ids
	  */
	override lazy val childId = property("childId")
	
	/**
	  * Database property used for interacting with order indices
	  */
	override lazy val orderIndex = property("orderIndex")
	
	
	// IMPLEMENTED	--------------------
	
	override def table = OptidepyTables.subSectionLink
	
	override def apply(data: SubSectionLinkData) = 
		apply(None, Some(data.parentId), Some(data.childId), Some(data.orderIndex))
	
	/**
	  * @param childId Id of the section contained within the parent section
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
	  * @param parentId Id of the parent section, which contains the child section
	  * @return A model containing only the specified parent id
	  */
	override def withParentId(parentId: Int) = apply(parentId = Some(parentId))
	
	override protected def complete(id: Value, data: SubSectionLinkData) = SubSectionLink(id.getInt, data)
}

/**
  * Used for interacting with SubSectionLinks in the database
  * @param id sub section link database id
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class SubSectionLinkDbModel(id: Option[Int] = None, parentId: Option[Int] = None, 
	childId: Option[Int] = None, orderIndex: Option[Int] = None) 
	extends PlacedLinkDbModel with PlacedLinkDbModelLike[SubSectionLinkDbModel] 
		with SubSectionLinkFactory[SubSectionLinkDbModel]
{
	// IMPLEMENTED	--------------------
	
	override def dbProps = SubSectionLinkDbModel
	
	override def table = SubSectionLinkDbModel.table
	
	/**
	  * @param id Id to assign to the new model (default = currently assigned id)
	  * @param parentId parent id to assign to the new model (default = currently assigned value)
	  * @param childId child id to assign to the new model (default = currently assigned value)
	  * @param orderIndex order index to assign to the new model (default = currently assigned value)
	  */
	override def copyPlacedLink(id: Option[Int] = id, parentId: Option[Int] = parentId, 
		childId: Option[Int] = childId, orderIndex: Option[Int] = orderIndex) = 
		copy(id = id, parentId = parentId, childId = childId, orderIndex = orderIndex)
}

