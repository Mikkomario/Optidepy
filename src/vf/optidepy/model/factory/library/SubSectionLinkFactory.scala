package vf.optidepy.model.factory.library

/**
  * Common trait for sub section link-related factories which allow construction with individual properties
  * @tparam A Type of constructed instances
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait SubSectionLinkFactory[+A] extends PlacedLinkFactory[A]

