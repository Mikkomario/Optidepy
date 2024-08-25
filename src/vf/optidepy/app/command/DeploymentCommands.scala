package vf.optidepy.app.command

import utopia.flow.collection.CollectionExtensions._
import utopia.flow.collection.immutable.Single
import utopia.flow.time.TimeExtensions._
import utopia.flow.util.console.{ArgumentSchema, Command}
import utopia.flow.util.console.ConsoleExtensions._
import vf.optidepy.app.action.DeploymentActions
import vf.optidepy.util.Common._

/**
 * Interface for accessing commands related to deployments
 * @author Mikko Hilpinen
 * @since 24.08.2024, v1.2
 */
object DeploymentCommands extends Commands
{
	// ATTRIBUTES   ---------------------------
	
	private val deployCommand = Command("deploy", "dep", help = "Deploys a project")(
		ArgumentSchema("target",
			help = "Name of the project and/or configuration to deploy. If specifying both, separate them with '/'. Optional."),
		ArgumentSchema("branch", "b",
			help = "Name of the branch to deploy. If left empty, the default branch will be targeted (if there is one)."),
		ArgumentSchema("since", "t",
			help = "Time or date, after which all changes should be included. Optional. Expects input such as \"2024-01-15\", \"last monday\" or \"7.8\"."),
		ArgumentSchema.flag("rebuild", "R",
			help = "Whether the project should be completely re-deployed, without checking which files have changed.")) {
		args =>
			// Parses the "since" parameter
			val since = args("since").instant
				.orElse { args("since").string.flatMap { parseDate(_).map { _.atStartOfDay.toInstantInDefaultZone } } }
			
			// Performs the deployment (interactive)
			cPool.tryWith { implicit c =>
				DeploymentActions.deploy(args("target").getString, args("branch").getString, since,
					args("rebuild").getBoolean)
			}.logFailure
	}
	
	override val commands: Seq[Command] = Single(deployCommand)
}
