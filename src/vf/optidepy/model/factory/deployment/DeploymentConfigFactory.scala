package vf.optidepy.model.factory.deployment

import java.nio.file.Path
import java.time.Instant

/**
  * Common trait for deployment config-related factories which allow construction with individual properties
  * @tparam A Type of constructed instances
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait DeploymentConfigFactory[+A]
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
	  * @param fileDeletionEnabled New file deletion enabled to assign
	  * @return Copy of this item with the specified file deletion enabled
	  */
	def withFileDeletionEnabled(fileDeletionEnabled: Boolean): A
	
	/**
	  * @param outputDirectory New output directory to assign
	  * @return Copy of this item with the specified output directory
	  */
	def withOutputDirectory(outputDirectory: Path): A
	
	/**
	  * @param projectId New project id to assign
	  * @return Copy of this item with the specified project id
	  */
	def withProjectId(projectId: Int): A
	
	/**
	  * @param relativeInputDirectory New relative input directory to assign
	  * @return Copy of this item with the specified relative input directory
	  */
	def withRelativeInputDirectory(relativeInputDirectory: Path): A
	
	/**
	  * @param usesBuildDirectories New uses build directories to assign
	  * @return Copy of this item with the specified uses build directories
	  */
	def withUsesBuildDirectories(usesBuildDirectories: Boolean): A
}

