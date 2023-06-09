package vf.optidepy.app

import utopia.flow.collection.CollectionExtensions._
import utopia.flow.collection.immutable.caching.cache.Cache
import utopia.flow.operator.EqualsExtensions._
import utopia.flow.parse.file.FileExtensions._
import utopia.flow.parse.file.container.ObjectsFileContainer
import utopia.flow.time.TimeExtensions._
import utopia.flow.util.console.ConsoleExtensions._
import utopia.flow.util.console.{ArgumentSchema, Command, Console}
import utopia.flow.util.StringExtensions._
import vf.optidepy.controller.{Deploy, IndexCounter, Merge}
import vf.optidepy.model.{Binding, DeployedProject, Project}
import vf.optidepy.util.Common._

import java.nio.file.{Path, Paths}
import scala.io.StdIn
import scala.util.{Failure, Success}

/**
 * The main application of this project
 * @author Mikko Hilpinen
 * @since 20.3.2023, v0.1
 */
object OptidepyApp extends App
{
	private lazy val projects = new ObjectsFileContainer(dataDirectory/"projects.json", DeployedProject)
	private val counters = Cache { p: DeployedProject =>
		new IndexCounter(p.deployments.map { _.index }.maxOption match {
			case Some(max) => max + 1
			case None => 1
		})
	}
	private var lastDeployment: Option[DeployedProject] = None
	
	private def findProject(projectName: String) = {
		// Finds the project
		val result = projectName.notEmpty match {
			case Some(projectName) => projects.current.find { _.name ~== projectName }
			case None => lastDeployment
		}
		// Displays a warning if no project was found
		if (result.isEmpty) {
			if (projects.current.isEmpty)
				println("Please add a new project first using the 'add' command")
			else {
				if (projectName.isEmpty)
					println("The name of the targeted project must be specified as a command argument")
				else
					println(s"$projectName didn't match any existing project")
				println("Registered projects:")
				projects.current.foreach { p => println(s"- ${p.name}") }
			}
		}
		result
	}
	private def deploy(project: DeployedProject, skipSeparateBuild: Boolean = false) = {
		println(s"Deploying ${ project.name }...")
		Deploy(project, skipSeparateBuild)(counters(project), log) match {
			case Success(project) =>
				lastDeployment = Some(project)
				projects.pointer.update { old => old.replaceOrAppend(project) { _.name == project.name } }
			case Failure(error) => log(error, s"Failed to deploy ${ project.name }")
		}
	}
	
	private val commands = Vector(
		Command("add", "a", help = "Registers a new project")(
			ArgumentSchema("project", "name", help = "Name of the new project"),
			ArgumentSchema("input", "in", help = "Path to the root input directory"),
			ArgumentSchema("output", "out", help = "Path to the root output directory")
		) { args =>
			if (args.containsValuesFor("project", "input")) {
				val projectName = args("project").getString
				val input = args("input").string.map { Paths.get(_) }
				val output: Path = args("output").stringOr(s"data/$projectName")
				println(s"Please specify directories or files${
					input.map { i => s" under $i" }.getOrElse("") } that should be copied during deployment.\nEmpty line ends input.")
				val bindings = StdIn.readLineIteratorWithPrompt("Next file").takeWhile { _.nonEmpty }.flatMap { inputLine =>
					val sourcePath: Path = inputLine
					val inputSourcePath = input match {
						case Some(i) => i/sourcePath
						case None => sourcePath
					}
					if (inputSourcePath.exists ||
						StdIn.ask(s"${ sourcePath.fileName } doesn't exist. Add it anyway?")) {
						val default = output/sourcePath
						val out = StdIn.readNonEmptyLine(s"Where do you want to store ${
							sourcePath.fileName } under $output?\nDefault = $sourcePath") match
						{
							case Some(out) => out: Path
							case None => default
						}
						Some(Binding(sourcePath, out))
					}
					else
						None
				}.toVector
				val newProject = DeployedProject(Project(projectName, input, output, bindings))
				projects.current :+= newProject
				println(s"$projectName added as a new project")
				if (StdIn.ask(s"Do you want to deploy $projectName now?"))
					deploy(newProject)
			}
			else
				println("Parameters 'project' and 'input' are required")
		},
		Command("deploy", "dep", "Deploys a project")(
			ArgumentSchema("project", "name", help = "Name of the project to deploy (default = last project)"),
			ArgumentSchema.flag("onlyFull", "F",
				help = "Whether no separate build directory should be created, resulting in only the \"full\" directory being created")) { args =>
			findProject(args("project").getString).foreach { deploy(_, args("onlyFull").getBoolean) }
		},
		Command("merge", "m", "Merges recent builds into a single build")(
			ArgumentSchema("project", "name", help = "Targeted project. Default = Last deployed project."),
			ArgumentSchema("since", "t",
				help = "Earliest targeted deployment date or time. Default = merge all previous builds.")) { args =>
			findProject(args("project").getString).foreach { project =>
				val since = args("since").instant
				since match {
					case Some(since) => println(s"Merges builds since ${ since.toLocalDateTime }...")
					case None => println("Merges all recent builds...")
				}
				Merge(project, since) match {
					case Success(result) =>
						result match {
							case Some(directory) =>
								if (StdIn.ask("Merging completed. Do you want to open the created directory?"))
									directory.openInDesktop().logFailure
							case None => println("There were no builds to merge")
						}
					case Failure(error) => log(error, "Merging failed")
				}
			}
		}
	)
	private val console = Console.static(commands, "Next command", "exit")
	console.registerToStopOnceJVMCloses()
	
	println("\nWelcome to Optidepy")
	println("Get more information about commands with 'help'. Close this console with 'exit'")
	console.run()
	println("Bye!")
}
