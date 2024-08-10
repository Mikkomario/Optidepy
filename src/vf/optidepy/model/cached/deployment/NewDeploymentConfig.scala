package vf.optidepy.model.cached.deployment

import java.nio.file.Path

/**
 * Contains information for constructing a new project deployment configuration et
 *
 * @param outputDirectory Directory where generated files will be placed
 * @param relativeInputDirectory Directory relative to project root, where all files are read from.
 *                               None if the same as the project root.
 * @param bindings Paths to the deployed files + where they're deployed.
 *                 Relative to the input and output directories.
 * @param usesBuildDirectories Whether this project places deployed files in separate build directories
 * @param fileDeletionEnabled Whether output files not present in the input directories should be automatically removed
 * @author Mikko Hilpinen
 * @since 10.08.2024, v1.2
 */
case class NewDeploymentConfig(outputDirectory: Path, relativeInputDirectory: Option[Path],
                               bindings: Seq[CachedBinding],
                               usesBuildDirectories: Boolean = true,
                               fileDeletionEnabled: Boolean = true)
