package vf.optidepy.app.action

import utopia.flow.collection.CollectionExtensions._
import utopia.flow.collection.immutable.{Empty, Pair, Single}
import utopia.flow.collection.mutable.iterator.OptionsIterator
import utopia.flow.parse.file.FileExtensions._
import utopia.flow.parse.string.Regex
import utopia.flow.util.console.ConsoleExtensions._
import utopia.flow.util.logging.Logger
import utopia.vault.database.Connection
import vf.optidepy.controller.dependency.IdeaFiles
import vf.optidepy.database.access.many.deployment.config.DbDeploymentConfigs
import vf.optidepy.database.access.many.project.DbProjectsWithModules
import vf.optidepy.database.access.single.project.DbProject
import vf.optidepy.database.storable.dependency.DependencyDbModel
import vf.optidepy.database.storable.library.VersionedModuleDbModel
import vf.optidepy.database.storable.project.ProjectDbModel
import vf.optidepy.model.cached.deployment.{CachedBinding, NewDeploymentConfig}
import vf.optidepy.model.cached.library.NewModule
import vf.optidepy.model.combined.project.DetailedProject
import vf.optidepy.model.partial.dependency.DependencyData
import vf.optidepy.model.partial.project.ProjectData

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
	// ATTRIBUTES   ---------------------
	
	private val changeListDocumentNameRegex = Regex.any + (Regex("c") || Regex("C")).withinParenthesis +
		Regex("hange") + Regex.any + Regex.escape('.') + Regex("md")
	private val nameSplitterRegex = Regex.escape('-') || Regex.escape('_')
	
	
	// OTHER    -------------------------
	
	/**
	 * Sets up a new project
	 * @param rootDirectory Directory that contains all project files
	 * @param log Implicit logging implementation
	 * @param connection Implicit DB connection
	 * @return Created project. None if the project-creation was canceled at some point.
	 */
	def setup(rootDirectory: Path)(implicit log: Logger, connection: Connection) = {
		// Identifies project name
		val defaultProjectName = rootDirectory.fileName
		val projectName = StdIn.readNonEmptyLine(
			s"What name do you want to use for this project? \nDefault = $defaultProjectName")
			.getOrElse(defaultProjectName)
		
		// Checks whether the project exists already
		if (DbProject.withName(projectName).nonEmpty)
			setupModules(rootDirectory) { modules =>
				// The second parameter here is the index of the connected versioned module, if applicable
				val deploymentConfig = {
					if (StdIn.ask("Do you want to specify deployment settings for this project?", default = true)) {
						Some(setupDeploymentConfig(rootDirectory, projectName)).filter { _.bindings.nonEmpty }
							// Connects this deployment to a module, if desirable and possible
							.map { deployment =>
								val connectedModuleIndex = modules.emptyOneOrMany match {
									case None => None
									case Some(Left(onlyModule)) =>
										if (StdIn.ask(s"Do you want to tie this deployment to versioned module ${
											onlyModule.name }? This enables versioned deployments.", default = true))
											Some(0)
										else
											None
									case Some(Right(modules)) =>
										println("If you want, you can connect this deployment to one of the versioned project modules. This enables versioned deployments.")
										StdIn.selectFrom(
											modules.zipWithIndex.map { case (m, index) => index -> m.name },
											"modules", "connect to")
								}
								deployment -> connectedModuleIndex
							}
					}
					else
						None
				}
				val dependencies = setupDependencies(rootDirectory)
				
				// Case: No deployments, modules or dependencies => Cancels
				if (modules.isEmpty && deploymentConfig.isEmpty && dependencies.isEmpty) {
					println("No data for this project was specified. \nProject creation canceled.")
					None
				}
				else {
					// Saves the project data
					val ideaPath = IdeaFiles.locate(rootDirectory)
					val project = ProjectDbModel
						.insert(ProjectData(projectName, rootDirectory, ideaPath.map { _.ideaDirectory }))
					val storedModules = VersionedModuleDbModel.insert(modules.map { _.toModuleUnder(project.id) })
					val storedDeploymentConfig = deploymentConfig match {
						case Some(config) => DbDeploymentConfigs.insert(project.id, Single(config))
						case None => Empty
					}
					val storedDependencies = DependencyDbModel.insert(
						dependencies.map { case (dependency, relativeLibPath) =>
							DependencyData(project.id, dependency.id, relativeLibPath)
						})
					
					// Combines and returns the project data
					Some(DetailedProject(project, storedModules, storedDeploymentConfig, storedDependencies))
				}
			}.flatten
		else {
			println(s"Project with name \"$projectName\" already exists. Cancels project creation.")
			None
		}
	}
	
	// 'f' is called unless the user cancels this process.
	// Returns None if canceled and Some with return value of 'f' if continued.
	private def setupModules[A](rootDirectory: Path)(f: IndexedSeq[NewModule] => A)(implicit log: Logger) = {
		// Looks for versioned modules
		val (partialModules, modules) = findModules(rootDirectory).logToOption.getOrElse(Empty -> Empty)
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
		if (partialModules.isEmpty || StdIn.ask(
			s"Following versioned modules didn't contain an artifact directory and will be ignored: \n\t- ${
				partialModules.mkString("\n\t- ") }\nIs it okay to continue nonetheless?", default = true))
			Some(f(modules))
		else {
			println("Project creation canceled")
			None
		}
	}
	
	private def setupDeploymentConfig(rootDirectory: Path, projectName: String) = {
		val relativeInput: Path = StdIn.readLine(
			s"Please specify the path common for all deployment inputs. \nSpecify a path relative to $rootDirectory\nIf empty, $rootDirectory will be used as the common root.")
		val input = rootDirectory/relativeInput
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
		
		println("Specify a name for this configuration. \nThis step is optional, but useful if you plan to use multiple configurations on this project.")
		val name = StdIn.readLine()
		
		NewDeploymentConfig(name, output, relativeInput, bindings, usesBuildDirectories, fileRemovalEnabled)
	}
	
	/**
	 * Interactively sets up project's dependencies to versioned modules from other projects
	 * @param rootDirectory Project root directory
	 * @param connection Implicit DB connection.
	 * @return Created dependencies where each entry contains 2 values:
	 *              1. The module this project depends on
	 *              1. Relative path to the directory where the module jars will be placed
	 */
	private def setupDependencies(rootDirectory: Path)(implicit connection: Connection) = {
		// Scans for projects with versioned modules
		val projects = DbProjectsWithModules.havingModules.pull
		
		// Case: No modules => Impossible to add any dependencies
		if (projects.isEmpty || !StdIn.ask("Do you want to add any dependencies to this project?"))
			Empty
		else {
			// Requests the dependencies to add from the user
			var remainingOptions = projects.flatMap { project =>
				project.modules.oneOrMany match {
					case Left(module) => Single(module -> module.name)
					case Right(modules) => modules.map { module => module -> s"${project.name}/${module.name}" }
				}
			}
			val dependencies = OptionsIterator
				.continually {
					if (remainingOptions.isEmpty)
						None
					else {
						println("\nPlease select the next dependency to add. Empty input stops adding dependencies.")
						val nextDependency = StdIn.selectFrom(remainingOptions, "modules", maxListCount = 30)
						// Removes the selected module from the available options
						nextDependency.foreach { module =>
							remainingOptions = remainingOptions.filterNot { _._1.id == module.id }
						}
						nextDependency
					}
				}
				.toOptimizedSeq
			
			if (dependencies.isEmpty)
				Empty
			else {
				// Determines the locations where these dependencies will be placed
				// Case: Places all dependency jars in the same directory
				if (dependencies.hasSize > 1 &&
					StdIn.ask("Do you want to place all these jars in the same directory?"))
				{
					val relativeLibDir = requestLibDirectory(rootDirectory, "these jars")
					dependencies.map { _ -> relativeLibDir }
				}
				// Case: Places each dependency separately
				else
					dependencies.map { module => module -> requestLibDirectory(rootDirectory, module.name) }
			}
		}
	}
	
	private def requestLibDirectory(rootDirectory: Path, placedModulesStr: String) = {
		println(s"In which directory shall $placedModulesStr be placed?")
		println(s"Please specify a path relative to $rootDirectory")
		println("Default = data/lib")
		val pathStr = StdIn.readNonEmptyLine().getOrElse("data/lib")
		pathStr: Path
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
						Single(part)
					else
						part.substring(0, upperCaseLocations.head) +:
							upperCaseLocations.paired.map { case Pair(start, end) => part.substring(start, end) } :+
							part.substring(upperCaseLocations.last)
				}
				val lowerModuleNameParts = moduleNameParts.map { _.toLowerCase }
				val moduleName = moduleNameParts.mkString(" ")
				// The matching is performed based on module name vs. directory name
				artifactDirectories.view
					.filter { dir =>
						val dirNameParts = nameSplitterRegex.split(dir.fileName).toVector.map { _.toLowerCase }
						lowerModuleNameParts.forall(dirNameParts.contains)
						// In case of multiple possible results, the shortest name is selected
					}
					.minByOption { _.fileName.length } match {
						// Case: Matching directory found
						case Some(artifactDirectory) =>
							Right(NewModule(moduleName, changeListPath, artifactDirectory))
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
	
	// Returns relative bindings
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
				Some(CachedBinding(sourcePath, out))
			}
			else
				None
		}.toVector
	}
}
