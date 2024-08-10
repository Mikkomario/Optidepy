package vf.optidepy.model.partial.library

import vf.optidepy.model.factory.library.PlacedLinkFactory

/**
  * Common trait for classes which provide read and copy access to placed link properties
  * @tparam Repr Implementing data class or data wrapper class
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait PlacedLinkDataLike[+Repr] extends HasPlacedLinkProps with PlacedLinkFactory[Repr]
{
	// ABSTRACT	--------------------
	
	/**
	  * Builds a modified copy of this placed link
	  * @param parentId New parent id to assign. Default = current value.
	  * @param childId New child id to assign. Default = current value.
	  * @param orderIndex New order index to assign. Default = current value.
	  * @return A copy of this placed link with the specified properties
	  */
	def copyPlacedLink(parentId: Int = parentId, childId: Int = childId, orderIndex: Int = orderIndex): Repr
	
	
	// IMPLEMENTED	--------------------
	
	override def withChildId(childId: Int) = copyPlacedLink(childId = childId)
	
	override def withOrderIndex(orderIndex: Int) = copyPlacedLink(orderIndex = orderIndex)
	
	override def withParentId(parentId: Int) = copyPlacedLink(parentId = parentId)
}

