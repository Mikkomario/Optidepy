package vf.optidepy.model.factory.library

import utopia.flow.util.Version

/**
  * Common trait for module release-related factories which allow construction with individual properties
  * @tparam A Type of constructed instances
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait ModuleReleaseFactory[+A]
{
	// ABSTRACT	--------------------
	
	/**
	  * @param docId New doc id to assign
	  * @return Copy of this item with the specified doc id
	  */
	def withDocId(docId: Int): A
	
	/**
	  * @param jarName New jar name to assign
	  * @return Copy of this item with the specified jar name
	  */
	def withJarName(jarName: String): A
	
	/**
	  * @param moduleId New module id to assign
	  * @return Copy of this item with the specified module id
	  */
	def withModuleId(moduleId: Int): A
	
	/**
	  * @param version New version to assign
	  * @return Copy of this item with the specified version
	  */
	def withVersion(version: Version): A
}

