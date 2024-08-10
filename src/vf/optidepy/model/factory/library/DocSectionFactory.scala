package vf.optidepy.model.factory.library

import java.time.Instant

/**
  * Common trait for doc section-related factories which allow construction with individual properties
  * @tparam A Type of constructed instances
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait DocSectionFactory[+A]
{
	// ABSTRACT	--------------------
	
	/**
	  * @param created New created to assign
	  * @return Copy of this item with the specified created
	  */
	def withCreated(created: Instant): A
	
	/**
	  * @param headerId New header id to assign
	  * @return Copy of this item with the specified header id
	  */
	def withHeaderId(headerId: Int): A
}

