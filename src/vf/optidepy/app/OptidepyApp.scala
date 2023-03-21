package vf.optidepy.app

import utopia.flow.collection.CollectionExtensions._
import utopia.flow.collection.immutable.caching.cache.Cache
import utopia.flow.operator.EqualsExtensions._
import utopia.flow.parse.file.FileExtensions._
import utopia.flow.parse.file.container.ObjectsFileContainer
import utopia.flow.util.console.ConsoleExtensions._
import utopia.flow.util.console.{ArgumentSchema, Command, Console}
import utopia.flow.util.StringExtensions._
import vf.optidepy.controller.{Deploy, IndexCounter}
import vf.optidepy.model.{Binding, DeployedProject, Project}
import vf.optidepy.util.Common._

import java.nio.file.Path
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
	
	private def deploy(project: DeployedProject) = {
		println(s"Deploying ${ project.name }...")
		Deploy(project)(counters(project), log) match {
			case Success(project) =>
				lastDeployment = Some(project)
				projects.pointer.update { old => old.replaceOrAppend(project) { _.name == project.name } }
				if (StdIn.ask(s"${ project.name } deployed. Do you want to open the output directory?"))
					project.output.openInDesktop().logFailure
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
				val input: Path = args("input").getString
				val output: Path = args("output").stringOr(s"data/$projectName")
				println(s"Please specify directories or files under ${
					input.fileName } that should be copied during deployment.\nEmpty line ends input.")
				val bindings = StdIn.readLineIteratorWithPrompt("Next file").takeWhile { _.nonEmpty }.flatMap { inputLine =>
					val sourcePath: Path = inputLine
					if ((input/sourcePath).exists ||
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
				val newProject = DeployedProject(Project(projectName, Binding(input, output), bindings))
				projects.current :+= newProject
				println(s"$projectName added as a new project")
				if (StdIn.ask(s"Do you want to deploy $projectName now?"))
					deploy(newProject)
			}
			else
				println("Parameters 'project' and 'input' are required")
		},
		Command("deploy", "dep", "deploys a project")(
			ArgumentSchema("project", "name", help = "Name of the project to deploy (default = last project)")) { args =>
			val projectName = args("project").getString
			val project = projectName.notEmpty match {
				case Some(projectName) => projects.current.find { _.name ~== projectName }
				case None => lastDeployment
			}
			project match {
				case Some(project) => deploy(project)
				case None =>
					if (projects.current.isEmpty)
						println("Please add a new project first using the 'add' command")
					else {
						if (projectName.isEmpty)
							println("The name of the targeted project must be specified as a command argument")
						else
							println(s"$projectName didn't match any existing project")
						println("Registered projects:")
						projects.current.foreach { p => println(s"- ${ p.name }") }
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
