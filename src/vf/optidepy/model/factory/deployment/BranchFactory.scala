package vf.optidepy.model.factory.deployment

import java.time.Instant

/**
  * Common trait for branch-related factories which allow construction with individual properties
  * @tparam A Type of constructed instances
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait BranchFactory[+A]
{
	// ABSTRACT	--------------------
	
	/**
	  * @param created New created to assign
	  * @return Copy of this item with the specified created
	  */
	def withCreated(created: Instant): A
	
	/**
	  * @param deprecatedAfter New deprecated after to assign
	  * @return Copy of this item with the specified deprecated after
	  */
	def withDeprecatedAfter(deprecatedAfter: Instant): A
	
	/**
	  * @param isDefault New is default to assign
	  * @return Copy of this item with the specified is default
	  */
	def withIsDefault(isDefault: Boolean): A
	
	/**
	  * @param name New name to assign
	  * @return Copy of this item with the specified name
	  */
	def withName(name: String): A
	
	/**
	  * @param projectId New project id to assign
	  * @return Copy of this item with the specified project id
	  */
	def withProjectId(projectId: Int): A
}

