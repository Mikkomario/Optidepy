package vf.optidepy.model.cached.deployment

import vf.optidepy.model.partial.deployment.DeploymentConfigData

import java.nio.file.Path

/**
 * Contains information for constructing a new project deployment configuration et
 * @param name Name of this specific configuration. May be empty.
 * @param outputDirectory Directory where generated files will be placed
 * @param relativeInputDirectory Directory relative to project root, where all files are read from.
 *                               Empty if the same as the project root.
 * @param bindings Paths to the deployed files + where they're deployed.
 *                 Relative to the input and output directories.
 * @param usesBuildDirectories Whether this project places deployed files in separate build directories
 * @param fileDeletionEnabled Whether output files not present in the input directories should be automatically removed
 * @author Mikko Hilpinen
 * @since 10.08.2024, v1.2
 */
case class NewDeploymentConfig(name: String, outputDirectory: Path, relativeInputDirectory: Path,
                               bindings: Seq[CachedBinding],
                               usesBuildDirectories: Boolean = true,
                               fileDeletionEnabled: Boolean = true)
{
	/**
	 * Converts this to deployment configuration data.
	 * NB: The bindings are not included.
	 * @param projectId Id of the project to assign this deployment to.
	 * @param moduleId Id of the module to link to this configuration (optional)
	 * @return Deployment config data based on this data.
	 */
	def toDeploymentConfigData(projectId: Int, moduleId: Option[Int] = None) = DeploymentConfigData(
		projectId, outputDirectory, relativeInputDirectory, name, moduleId,
		usesBuildDirectories = usesBuildDirectories, fileDeletionEnabled = fileDeletionEnabled)
}