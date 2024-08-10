package vf.optidepy.model.factory.dependency

import java.nio.file.Path
import java.time.Instant

/**
  * Common trait for dependency-related factories which allow construction with individual properties
  * @tparam A Type of constructed instances
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait DependencyFactory[+A]
{
	// ABSTRACT	--------------------
	
	/**
	  * @param created New created to assign
	  * @return Copy of this item with the specified created
	  */
	def withCreated(created: Instant): A
	
	/**
	  * @param dependentProjectId New dependent project id to assign
	  * @return Copy of this item with the specified dependent project id
	  */
	def withDependentProjectId(dependentProjectId: Int): A
	
	/**
	  * @param deprecatedAfter New deprecated after to assign
	  * @return Copy of this item with the specified deprecated after
	  */
	def withDeprecatedAfter(deprecatedAfter: Instant): A
	
	/**
	  * @param libraryFilePath New library file path to assign
	  * @return Copy of this item with the specified library file path
	  */
	def withLibraryFilePath(libraryFilePath: Path): A
	
	/**
	  * @param relativeLibDirectory New relative lib directory to assign
	  * @return Copy of this item with the specified relative lib directory
	  */
	def withRelativeLibDirectory(relativeLibDirectory: Path): A
	
	/**
	  * @param usedModuleId New used module id to assign
	  * @return Copy of this item with the specified used module id
	  */
	def withUsedModuleId(usedModuleId: Int): A
}

