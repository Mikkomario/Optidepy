package vf.optidepy.model.cached.library

import vf.optidepy.model.partial.library.VersionedModuleData

import java.nio.file.Path

/**
 * Contains information needed for setting up a new versioned module
 * @param name Name of this module
 * @param relativeChangeListPath A path relative to the project root directory,
 *                               which points to this module's Changes.md file
 * @param relativeArtifactDirectory A path relative to the project root directory,
 *                                  which points to the directory where artifact jar files will be exported
 * @author Mikko Hilpinen
 * @since 10.08.2024, v1.2
 */
case class NewModule(name: String, relativeChangeListPath: Path, relativeArtifactDirectory: Path)
{
	/**
	 * @param projectId Project id attached to this module data
	 * @return Module data including this information, plus the specified project id
	 */
	def toModuleUnder(projectId: Int) =
		VersionedModuleData(projectId, name, relativeChangeListPath, relativeArtifactDirectory)
}