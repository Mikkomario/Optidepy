package vf.optidepy.model.factory.library

/**
  * Common trait for classes that implement SubSectionLinkFactory by wrapping a SubSectionLinkFactory instance
  * @tparam A Type of constructed instances
  * @tparam Repr Implementing type of this factory
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait SubSectionLinkFactoryWrapper[A <: SubSectionLinkFactory[A], +Repr] 
	extends SubSectionLinkFactory[Repr] with PlacedLinkFactoryWrapper[A, Repr]

