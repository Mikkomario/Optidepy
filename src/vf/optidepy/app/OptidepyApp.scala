package vf.optidepy.app

import utopia.flow.collection.CollectionExtensions._
import utopia.flow.collection.immutable.caching.cache.Cache
import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.operator.equality.EqualsExtensions._
import utopia.flow.parse.file.FileExtensions._
import utopia.flow.parse.file.container.ObjectsFileContainer
import utopia.flow.parse.string.Regex
import utopia.flow.time.{Now, Today, WeekDays}
import utopia.flow.time.TimeExtensions._
import utopia.flow.time.WeekDays.MondayToSunday
import utopia.flow.util.console.ConsoleExtensions._
import utopia.flow.util.console.{ArgumentSchema, Command, Console}
import utopia.flow.util.StringExtensions._
import vf.optidepy.controller.{Deploy, IndexCounter, Merge}
import vf.optidepy.model.{Binding, DeployedProject, Project}
import vf.optidepy.util.Common._

import java.nio.file.{Path, Paths}
import scala.concurrent.duration.Duration
import scala.io.StdIn
import scala.util.{Failure, Success}

/**
 * The main application of this project
 * @author Mikko Hilpinen
 * @since 20.3.2023, v0.1
 */
object OptidepyApp extends App
{
	private implicit val weekDays: WeekDays = MondayToSunday
	
	private lazy val projects = new ObjectsFileContainer(dataDirectory/"projects.json", DeployedProject)
	private val counters = Cache { p: DeployedProject =>
		new IndexCounter(p.lastDeploymentIndex match {
			case Some(max) => max + 1
			case None => 1
		})
	}
	private var lastDeploymentAndBranch: Option[(DeployedProject, String)] = None
	private def lastDeployment = lastDeploymentAndBranch.map { _._1 }
	
	private val projectArg = ArgumentSchema("project", "name",
		help = "Name of the project to deploy (default = last project)")
	private val branchArg = ArgumentSchema("branch", "b",
		help = s"Targeted branch to deploy. Default value is the last deployed branch or ${
			DeployedProject.defaultBranchName}")
	private val commands = Vector(
		Command("describe", "desc", help = "Describes a registered project")(projectArg) { args =>
			findProject(args("project").getString).foreach { project =>
				println(s"\n${project.name}")
				val inputStr = project.input match {
					case Some(i) => i.toString
					case None => "Not defined"
				}
				println(s"\t- Root input: $inputStr")
				println(s"\t- Root output: ${project.output}")
				println(s"\t- Bindings:")
				project.relativeBindings.foreach { binding =>
					println(s"\t\t- ${binding.source} => ${binding.target}")
				}
				if (!project.usesBuildDirectories)
					println("\t- Only deploys to the \"full\" directory")
				if (!project.fileDeletionEnabled)
					println(s"\t- File deletion disabled")
				
				project.deployments.foreach { case (branch, deployments) =>
					deployments.lastOption.foreach { lastDeployment =>
						println(s"\t- Last deployment of $branch (${lastDeployment.index}) was made ${
							(Now - lastDeployment.timestamp).description } ago")
					}
				}
			}
		},
		Command("add", "a", help = "Registers a new project")(
			projectArg,
			ArgumentSchema("input", "in", help = "Path to the root input directory"),
			ArgumentSchema("output", "out", help = "Path to the root output directory")
		) { args =>
			if (args.containsValuesFor("project", "input")) {
				val projectName = args("project").getString
				// The project name must be unique
				if (projects.current.exists { _.name ~== projectName })
					println("Project with the same name exists already. Terminates.")
				else {
					val input = args("input").string.map { Paths.get(_) }
					val output: Path = args("output").stringOr(s"data/$projectName")
					println(s"Please specify directories or files${
						input.map { i => s" under $i" }.getOrElse("")
					} that should be copied during deployment.\nEmpty line ends input.")
					val bindings = collectBindings(input, output)
					val usesBuildDirectories = StdIn.ask(
						"Do you want to collect changed files to separate build directories?", default = true)
					val fileRemovalEnabled = StdIn.ask(
						"Should automatic deletion of non-source files be enabled?", default = true)
					val newProject = DeployedProject(Project(projectName, input, output, bindings,
						usesBuildDirectories, fileRemovalEnabled))
					projects.current :+= newProject
					println(s"$projectName added as a new project")
					if (StdIn.ask(s"Do you want to deploy $projectName now?"))
						deploy(newProject, args("branch").string,
							skipSeparateBuild = true, skipFileRemoval = true)
				}
			}
			else
				println("Parameters 'project' and 'input' are required")
		},
		Command("edit", "e", help = "Modifies the deployment paths of a single project")(projectArg) { args =>
			findProject(args("project").getString).foreach { project =>
				// Checks whether the main input path needs to be changed
				val (input, validInputBindings) = {
					val currentInputName = project.input match {
						case Some(p) => p.toString
						case None => "not defined"
					}
					if (StdIn.ask(s"Do you want to change the root input directory? Currently $currentInputName")) {
						val newRoot = StdIn.printAndReadLine(
								"Please specify the new input directory (leave empty if there is no common root directory)")
							.notEmpty.map { Paths.get(_) }
						// Transfers the input paths to the new root, if possible
						val newProject = project.withInput(newRoot)
						newRoot -> newProject.relativeBindings
					}
					else
						project.input -> project.relativeBindings
				}
				// Makes sure the input directory exists
				if (input.exists { _.notExists })
					println(s"The specified input directory (${input.get.toAbsolutePath}) doesn't exist. Terminates.")
				else {
					// Also, checks whether the output needs to be changed
					val output = {
						if (StdIn.ask(s"Do you want to change the root output directory? Currently ${project.output}"))
							StdIn.readNonEmptyLine("Please specify the new output directory",
									"Leaving this empty cancels project edit. \nPlease specify the directory if you wish to continue.")
								.map { Paths.get(_) -> Vector() }
						else
							Some(project.output -> validInputBindings)
					}
					output.foreach { case (output, existingBindings) =>
						// Checks whether the user wishes to edit/remove some of the existing bindings
						val remainingBindings = {
							if (existingBindings.isEmpty ||
								!StdIn.ask(s"Do you want to remove or replace any of the ${
									existingBindings.size} file mappings?"))
								existingBindings
							else
								existingBindings.filter { binding =>
									StdIn.ask(s"Do you wish to keep ${binding.source} => ${binding.target}?")
								}
						}
						// Collects new file bindings
						val newBindings = collectBindings(input, output)
						// Modifies the flags, if necessary
						// WET WET
						val usesBuildDirectories = StdIn.ask(
							"Do you want to collect changed files to separate build directories?",
							default = project.usesBuildDirectories)
						val fileRemovalEnabled = StdIn.ask(
							"Should automatic deletion of non-source files be enabled?",
							default = project.fileDeletionEnabled)
						// Saves the changes
						val modifiedProject = project.modify {
							_.copy(input = input, output = output, relativeBindings = remainingBindings ++ newBindings,
								usesBuildDirectories = usesBuildDirectories, fileDeletionEnabled = fileRemovalEnabled)
						}
						projects.pointer.update { _.filterNot { _ == project } :+ modifiedProject }
						println("Changes saved!")
					}
				}
			}
		},
		Command("deploy", "dep", "Deploys a project")(
			projectArg, branchArg,
			ArgumentSchema("since", "t", help =
				"The duration of time to always include in this build"),
			ArgumentSchema.flag("onlyFull", "F",
				help = "Whether no separate build directory should be created, resulting in only the \"full\" directory being created"),
			ArgumentSchema.flag("noRemoval", "NR", help = "Whether the file deletion process should be skipped")) { args =>
			findProject(args("project").getString)
				.foreach { project =>
					// Parses the 'since' parameter input
					val since = args("since").string.flatMap[Duration] { input =>
						// Expects either number + unit or a specific input like "last week"
						// "Left" items are non-number, "Right" items are numbers
						val parts = Regex.number.divide(input).take(2)
						// Takes the number part (first part), if present and attempts to parse it
						parts.headOption.flatMap { _.toOption.flatMap { _.int } } match {
							// Case: 'since' is specified as number + input
							case Some(number) =>
								// Parses the unit part
								val unit = parts.lift(1) match {
									case Some(part) => part.either
									case None => ""
								}
								// Case: Unit not specified => Warns the user
								if (unit.isEmpty) {
									println("When specifying the 'since' parameter, you must also specify the unit (s, m, h, w)")
									None
								}
								else
									unit.head.toLower match {
										// Case: Hours
										case 'h' => Some(number.hours)
										// Case: Seconds
										case 's' => Some(number.seconds)
										// Case: Minutes
										case 'm' => Some(number.minutes)
										// Case: Weeks
										case 'w' => Some(number.weeks)
										// Case: Unsupported unit
										case _ =>
											println(s"The specified unit '$unit' is not recognized as a valid time unit.\nPlease use one of the following: s, m, h, w")
											None
									}
							// Case: No number is specified => Expects specific input options
							case None =>
								input.toLowerCase match {
									case "today" => Some(Now - Today.toInstantInDefaultZone)
									case "yesterday" => Some(Now - Today.yesterday.toInstantInDefaultZone)
									case "this week" =>
										Some(Now - Today.previous(weekDays.first, includeSelf = true)
											.toInstantInDefaultZone)
									case "last week" =>
										Some(Now - Today.previous(weekDays.first).toInstantInDefaultZone)
									case "this month" =>
										Some(Now - Today.yearMonth.firstDay.toInstantInDefaultZone)
									case "last month" =>
										Some(Now - Today.yearMonth.previous.firstDay.toInstantInDefaultZone)
									case other =>
										println(s"The specified 'since' parameter input '$other' is not supported")
										None
								}
						}
					}
					
					deploy(project, args("branch").string, since,
						args("onlyFull").getBoolean, args("noRemoval").getBoolean)
				}
		},
		Command("merge", "m", "Merges recent builds into a single build")(
			projectArg, branchArg,
			ArgumentSchema("branch", "b", DeployedProject.defaultBranchName, help = "Targeted branch to deploy"),
			ArgumentSchema("since", "t",
				help = "Earliest targeted deployment date or time. Default = merge all previous builds.")) { args =>
			findProject(args("project").getString).foreach { project =>
				val since = args("since").instant
				since match {
					case Some(since) => println(s"Merges builds since ${ since.toLocalDateTime }...")
					case None => println("Merges all recent builds...")
				}
				Merge(project, args("branch").stringOr(DeployedProject.defaultBranchName), since) match {
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
	private val console = Console.static(commands, "\nNext command", "exit")
	console.registerToStopOnceJVMCloses()
	
	println("\nWelcome to Optidepy")
	println("Get more information about commands with 'help'. Close this console with 'exit'")
	console.run()
	println("Bye!")
	
	
	// OTHER FUNCTIONS  ------------------------
	
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
	
	private def deploy(project: DeployedProject, branch: Option[String], since: Option[Duration] = None,
	                   skipSeparateBuild: Boolean = false, skipFileRemoval: Boolean = false) =
	{
		println(s"Deploying ${project.name}...")
		// If branch name is not specified, uses the last deployed branch,
		// but only if targeting the same project
		val usedBranch = branch.getOrElse {
			lastDeploymentAndBranch.filter { _._1.name == project.name }.map { _._2 }
				.getOrElse(DeployedProject.defaultBranchName)
		}
		// Covers the case where the "since" duration is infinite
		val appliedSince = since.flatMap { _.finite.map { Now - _ } }
		Deploy(project, usedBranch, appliedSince, skipSeparateBuild,
			skipFileRemoval)(counters(project), log) match
		{
			case Success(project) =>
				lastDeploymentAndBranch = Some(project -> usedBranch)
				projects.pointer.update { old => old.replaceOrAppend(project) { _.name == project.name } }
				println(s"${project.name} successfully deployed")
			case Failure(error) => log(error, s"Failed to deploy ${project.name}")
		}
	}
	
	private def collectBindings(input: Option[Path], output: Path) = {
		println(s"Please specify directories or files${
			input.map { i => s" under $i" }.getOrElse("")
		} that should be copied during deployment.\nEmpty line ends input.")
		StdIn.readLineIteratorWithPrompt("Next file").takeWhile { _.nonEmpty }.flatMap { inputLine =>
			val sourcePath: Path = inputLine
			val inputSourcePath = input match {
				case Some(i) => i / sourcePath
				case None => sourcePath
			}
			if (inputSourcePath.exists ||
				StdIn.ask(s"${sourcePath.fileName} doesn't exist. Add it anyway?")) {
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
