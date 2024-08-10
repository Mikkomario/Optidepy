package vf.optidepy.model.factory.library

/**
  * Common trait for classes that implement DocLineLinkFactory by wrapping a DocLineLinkFactory instance
  * @tparam A Type of constructed instances
  * @tparam Repr Implementing type of this factory
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait DocLineLinkFactoryWrapper[A <: DocLineLinkFactory[A], +Repr] 
	extends DocLineLinkFactory[Repr] with PlacedLinkFactoryWrapper[A, Repr]
{
	// IMPLEMENTED	--------------------
	
	override def withSectionId(sectionId: Int) = withParentId(sectionId)
	override def withTextId(textId: Int) = withChildId(textId)
}

