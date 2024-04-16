package vf.optidepy.model.dependency

import java.nio.file.Path

/**
 * Represents a project's dependency on another project.
 * @param libraryId Id of the library this project depends on
 * @param moduleDependencies List of dependencies on that library's modules
 * @param relativeArtifactsDirPath Path relative to the application root directory,
 *                                 which contains the project artifacts which describe
 *                                 how project code and dependencies are to be exported.
 *                                 None if not applicable to this project.
 * @author Mikko Hilpinen
 * @since 10.04.2024, v1.2
 */
@deprecated("Deprecated for removal", "v1.2")
case class LibraryDependency(libraryId: String, moduleDependencies: Set[ModuleDependency],
                             relativeArtifactsDirPath: Option[Path] = None)
