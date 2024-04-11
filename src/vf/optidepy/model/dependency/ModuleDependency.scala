package vf.optidepy.model.dependency

import java.nio.file.Path

/**
 * Represents an application's dependency on a single versioned library module
 * @param moduleId Id of the module on which the described application is dependent upon
 * @param relativeJarDirectory Path relative to the application root directory,
 *                             which points to the directory where module jars are stored
 * @param relativeLibraryFilePath Path relative to the application root directory,
 *                                which specifies which jar is loaded as a library into the project.
 *                                None if no such file is used.
 * @author Mikko Hilpinen
 * @since 10.04.2024, v1.2
 */
case class ModuleDependency(moduleId: String, relativeJarDirectory: Path, relativeLibraryFilePath: Option[Path] = None)
