package vf.optidepy.model.factory.library

/**
  * Common trait for doc line link-related factories which allow construction with individual properties
  * @tparam A Type of constructed instances
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait DocLineLinkFactory[+A] extends PlacedLinkFactory[A]
{
	// ABSTRACT	--------------------
	
	/**
	  * @param sectionId New section id to assign
	  * @return Copy of this item with the specified section id
	  */
	def withSectionId(sectionId: Int): A
	
	/**
	  * @param textId New text id to assign
	  * @return Copy of this item with the specified text id
	  */
	def withTextId(textId: Int): A
	
	
	// IMPLEMENTED	--------------------
	
	override def withChildId(childId: Int) = withTextId(childId)
	
	override def withParentId(parentId: Int) = withSectionId(parentId)
}

