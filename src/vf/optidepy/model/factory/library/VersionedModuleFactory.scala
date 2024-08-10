package vf.optidepy.model.factory.library

import java.nio.file.Path
import java.time.Instant

/**
  * Common trait for versioned module-related factories which allow construction with individual properties
  * @tparam A Type of constructed instances
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait VersionedModuleFactory[+A]
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
	  * @param name New name to assign
	  * @return Copy of this item with the specified name
	  */
	def withName(name: String): A
	
	/**
	  * @param projectId New project id to assign
	  * @return Copy of this item with the specified project id
	  */
	def withProjectId(projectId: Int): A
	
	/**
	  * @param relativeArtifactDirectory New relative artifact directory to assign
	  * @return Copy of this item with the specified relative artifact directory
	  */
	def withRelativeArtifactDirectory(relativeArtifactDirectory: Path): A
	
	/**
	  * @param relativeChangeListPath New relative change list path to assign
	  * @return Copy of this item with the specified relative change list path
	  */
	def withRelativeChangeListPath(relativeChangeListPath: Path): A
}

