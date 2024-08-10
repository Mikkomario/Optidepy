package vf.optidepy.model.stored.library

import utopia.vault.model.template.{FromIdFactory, Stored}
import vf.optidepy.model.factory.library.PlacedLinkFactoryWrapper
import vf.optidepy.model.partial.library.PlacedLinkDataLike

/**
  * Common trait for placed links which have been stored in the database
  * @tparam Data Type of the wrapped data
  * @tparam Repr Implementing type
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait StoredPlacedLinkLike[Data <: PlacedLinkDataLike[Data], +Repr]
	extends Stored[Data, Int] with FromIdFactory[Int, Repr] with PlacedLinkFactoryWrapper[Data, Repr]
		with PlacedLinkDataLike[Repr]
{
	// IMPLEMENTED	--------------------
	
	override def childId = data.childId
	override def orderIndex = data.orderIndex
	override def parentId = data.parentId
	
	override protected def wrappedFactory = data
	
	override def copyPlacedLink(parentId: Int, childId: Int, orderIndex: Int) = 
		wrap(data.copyPlacedLink(parentId, childId, orderIndex))
}

