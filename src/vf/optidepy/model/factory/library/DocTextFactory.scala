package vf.optidepy.model.factory.library

import java.time.Instant

/**
  * Common trait for doc text-related factories which allow construction with individual properties
  * @tparam A Type of constructed instances
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait DocTextFactory[+A]
{
	// ABSTRACT	--------------------
	
	/**
	  * @param created New created to assign
	  * @return Copy of this item with the specified created
	  */
	def withCreated(created: Instant): A
	
	/**
	  * @param indentation New indentation to assign
	  * @return Copy of this item with the specified indentation
	  */
	def withIndentation(indentation: Int): A
	
	/**
	  * @param text New text to assign
	  * @return Copy of this item with the specified text
	  */
	def withText(text: String): A
}

