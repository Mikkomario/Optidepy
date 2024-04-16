package vf.optidepy.app.action

import utopia.flow.parse.file.FileExtensions._
import utopia.flow.util.console.ConsoleExtensions._

import java.nio.file.Path
import scala.io.StdIn

/**
 * Contains interactive actions for identifying and setting up projects
 *
 * @author Mikko Hilpinen
 * @since 15.04.2024, v1.2
 */
object ProjectActions
{
	def setup(rootDirectory: Path) = {
		/*
		(name: String, rootPath: Path, modules: Vector[VersionedModuleWithReleases] = Vector.empty,
                   deploymentConfig: Option[ProjectDeployments] = None,
                   moduleDependencies: Vector[ModuleDependency] = Vector.empty,
                   relativeArtifactsDirPath: Option[Path] = None)

		 */
		// Identifies project name
		val defaultProjectName = rootDirectory.fileName
		val projectName = StdIn.readNonEmptyLine(
			s"What name do you want to use for this project? \nDefault = $defaultProjectName")
			.getOrElse(defaultProjectName)
		
		// Looks for versioned modules
		
	}
}
