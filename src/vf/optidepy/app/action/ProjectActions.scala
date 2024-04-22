package vf.optidepy.app.action

import utopia.flow.collection.CollectionExtensions._
import utopia.flow.collection.immutable.Pair
import utopia.flow.parse.file.FileExtensions._
import utopia.flow.parse.string.Regex
import utopia.flow.util.console.ConsoleExtensions._
import utopia.flow.util.logging.Logger
import vf.optidepy.model.deployment.{Binding, ProjectDeploymentConfig}
import vf.optidepy.model.library.VersionedModule

import java.nio.file.Path
import java.util.UUID
import scala.io.StdIn

/**
 * Contains interactive actions for identifying and setting up projects
 *
 * @author Mikko Hilpinen
 * @since 15.04.2024, v1.2
 */
object ProjectActions
{
	// ATTRIBUTES   ---------------------
	
	private val changeListDocumentNameRegex = Regex.any + (Regex("c") || Regex("C")).withinParenthesis +
		Regex("hange") + Regex.any + Regex.escape('.') + Regex("md")
	private val nameSplitterRegex = Regex.escape('-') || Regex.escape('_')
	
	
	// OTHER    -------------------------
	
	def setup(rootDirectory: Path)(implicit log: Logger) = {
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
		
		// TODO: Make sure this project doesn't exist yet
		
		// Looks for versioned modules
		val (partialModules, modules) = findModules(rootDirectory).logToOption.getOrElse(Vector.empty -> Vector.empty)
		// Prints the results
		if (modules.isEmpty) {
			if (partialModules.isEmpty)
				println("This project doesn't seem to use versioning")
		}
		else if (modules.hasSize(1))
			println("This project uses versioning")
		else {
			println(s"Found ${ modules.size } versioned sub-modules:")
			modules.foreach { module => println(s"\t- ${ module.name }") }
		}
		
		// Handles the case where there are partial modules (these are ignored if the user chooses to continue)
		if (partialModules.nonEmpty || StdIn.ask(
			s"Following versioned modules didn't contain an artifact directory and will be ignored: \n\t- ${
				partialModules.mkString("\n\t- ") }\nIs it okay to continue nonetheless?", default = true))
		{
			val deploymentConfig = {
				if (StdIn.ask("Do you also want to specify deployment settings for this project?", default = true))
					Some(setupDeploymentConfig(rootDirectory, projectName)).filter { _.relativeBindings.nonEmpty }
				else
					None
			}
			
			if (modules.isEmpty && deploymentConfig.isEmpty)
				println("No versioned modules found and no deployments were specified. \nProject creation canceled.")
			else {
				// TODO: Add library dependencies
			}
		}
		else
			println("Project creation canceled")
	}
	
	private def setupDeploymentConfig(rootDirectory: Path, projectName: String) = {
		val input = StdIn.readNonEmptyLine(
			s"Please specify the path common for all deployment inputs. \nSpeficy a path relative to $rootDirectory\nIf empty, $rootDirectory will be used as the common root.") match
		{
			case Some(input) => rootDirectory/input
			case None => rootDirectory
		}
		val defaultOutput: Path = s"out/$projectName"
		val output = StdIn
			.readNonEmptyLine(s"Please specify the path where output build directories will be placed. \nDefault = ${
				defaultOutput.absolute }") match
		{
			case Some(input) => input: Path
			case None => defaultOutput
		}
		println(s"Please specify directories or files under $input that should be copied during deployment.\nEmpty line ends input.")
		val bindings = collectBindings(input, output)
		val usesBuildDirectories = StdIn.ask(
			"Do you want to collect changed files to separate build directories?", default = true)
		val fileRemovalEnabled = StdIn.ask(
			"Should automatic deletion of non-source files be enabled?", default = true)
		ProjectDeploymentConfig(projectName, Some(input), output, bindings, usesBuildDirectories, fileRemovalEnabled)
	}
	
	/**
	 * Finds project modules from the specified project directory
	 * @param projectDirectory Project's directory
	 * @return Modules that are missing an artifact directory, followed by modules under the targeted project.
	 *         Failure if file reading failed at some point
	 */
	private def findModules(projectDirectory: Path)(implicit log: Logger) = {
		// Finds directories which contain a change list document
		val modulePaths = findModuleDirectories(projectDirectory)
		// Next, makes sure the modules have an associated artifact
		val artifactsDirectory = projectDirectory/"out/artifacts"
		// Finds all possible specific artifact directories
		artifactsDirectory.iterateChildren { _.filter { _.isDirectory }.toVector }.map { artifactDirectories =>
			// Finds, which artifact directory matches with which module
			modulePaths.divideWith { case (moduleDir, changeListPath) =>
				// Splits the module name so that searching is more flexible
				val moduleNameParts = nameSplitterRegex.split(moduleDir.fileName).toVector.flatMap { part =>
					// Also splits the module name based on camel casing (e.g. TestModule -> Test & Module)
					val upperCaseLocations = Regex.upperCaseLetter.startIndexIteratorIn(part)
						.toVector.dropWhile { _ == 0 }
					if (upperCaseLocations.isEmpty)
						Vector(part)
					else
						part.substring(0, upperCaseLocations.head) +:
							upperCaseLocations.paired.map { case Pair(start, end) => part.substring(start, end) } :+
							part.substring(upperCaseLocations.last)
				}
				val lowerModuleNameParts = moduleNameParts.map { _.toLowerCase }
				val moduleName = moduleNameParts.mkString(" ")
				// The matching is performed based on module name vs. directory name
				artifactDirectories.filter { dir =>
					val dirNameParts = nameSplitterRegex.split(dir.fileName).toVector.map { _.toLowerCase }
					lowerModuleNameParts.forall(dirNameParts.contains)
					// In case of multiple possible results, the shortest name is selected
				}.minByOption { _.fileName.length } match {
					// Case: Matching directory found
					case Some(artifactDirectory) =>
						Right(VersionedModule(UUID.randomUUID().toString, moduleName, changeListPath, artifactDirectory))
					// Case: No matching directory found
					case None => Left(moduleName)
				}
			}
		}
	}
	
	// Returns project directories, including the associated changes documents
	private def findModuleDirectories(projectDirectory: Path)(implicit log: Logger) =
		// Includes nested directories in the search up to a certain point
		projectDirectory.toTree.nodesBelowIteratorUpToDepth(3)
			// All directories that contain a Changes.md document are considered modules
			.flatMap { node =>
				node.children.find { p => p.nav.isRegularFile && changeListDocumentNameRegex(p.nav.fileName) }
					.map { changesDocument => node.nav -> changesDocument.nav }
			}
			.toVector
	
	private def collectBindings(input: Path, output: Path) = {
		println(s"Please specify directories or files under $input that should be copied during deployment.\nEmpty line ends input.")
		StdIn.readLineIteratorWithPrompt("Next file").takeWhile { _.nonEmpty }.flatMap { inputLine =>
			val sourcePath: Path = inputLine
			val inputSourcePath = input/inputLine
			if (inputSourcePath.exists ||
				StdIn.ask(s"${inputSourcePath.absolute} doesn't exist. Add it anyway?")) {
				val default = output / sourcePath
				val out = StdIn.readNonEmptyLine(s"Where do you want to store ${
					sourcePath.fileName
				} under $output?\nDefault = $sourcePath") match {
					case Some(out) => out: Path
					case None => default
				}
				Some(Binding(sourcePath, out))
			}
			else
				None
		}.toVector
	}
}
