package vf.optidepy.model.factory.library

/**
  * Common trait for placed link-related factories which allow construction with individual properties
  * @tparam A Type of constructed instances
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait PlacedLinkFactory[+A]
{
	// ABSTRACT	--------------------
	
	/**
	  * @param childId New child id to assign
	  * @return Copy of this item with the specified child id
	  */
	def withChildId(childId: Int): A
	
	/**
	  * @param orderIndex New order index to assign
	  * @return Copy of this item with the specified order index
	  */
	def withOrderIndex(orderIndex: Int): A
	
	/**
	  * @param parentId New parent id to assign
	  * @return Copy of this item with the specified parent id
	  */
	def withParentId(parentId: Int): A
}

