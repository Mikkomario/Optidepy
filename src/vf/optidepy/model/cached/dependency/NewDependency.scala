package vf.optidepy.model.cached.dependency

import java.nio.file.Path

/**
 * Contains information for creating a new project dependency
 * @param usedModuleId Id of the module the referenced project uses
 * @param relativeLibDirectory Path to the directory where library jars will be placed.
 *                             Relative to the project's root path.
 * @param libraryFilePath Path to the library file matching this dependency. Relative to the project's root path.
 *                        None if not applicable.
 * @author Mikko Hilpinen
 * @since 10.08.2024, v1.2
 */
case class NewDependency(usedModuleId: Int, relativeLibDirectory: Path, libraryFilePath: Option[Path] = None)
