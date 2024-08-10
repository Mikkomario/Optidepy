package vf.optidepy.model.factory.dependency

import java.time.Instant

/**
  * Common trait for dependency update-related factories which allow construction with individual properties
  * @tparam A Type of constructed instances
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait DependencyUpdateFactory[+A]
{
	// ABSTRACT	--------------------
	
	/**
	  * @param created New created to assign
	  * @return Copy of this item with the specified created
	  */
	def withCreated(created: Instant): A
	
	/**
	  * @param dependencyId New dependency id to assign
	  * @return Copy of this item with the specified dependency id
	  */
	def withDependencyId(dependencyId: Int): A
	
	/**
	  * @param releaseId New release id to assign
	  * @return Copy of this item with the specified release id
	  */
	def withReleaseId(releaseId: Int): A
}

