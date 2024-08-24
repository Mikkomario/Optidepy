package vf.optidepy.app.command

import utopia.flow.collection.CollectionExtensions._
import utopia.flow.collection.immutable.Single
import utopia.flow.parse.file.FileExtensions._
import utopia.flow.parse.file.FileUtils
import utopia.flow.util.console.{ArgumentSchema, Command}
import vf.optidepy.app.action.ProjectActions
import vf.optidepy.util.Common._

import java.nio.file.Paths
import scala.annotation.unused
import scala.io.StdIn
import scala.language.implicitConversions

/**
 * An interface for accessing interactive project-related commands
 *
 * @author Mikko Hilpinen
 * @since 23.08.2024, v1.2
 */
object ProjectCommands
{
	// ATTRIBUTES   ----------------------------------
	
	private val setupProjectCommand = Command("setup", "new", help = "Sets up a new project")(
		ArgumentSchema("root", help = s"Project root directory. Absolute or relative to ${
			FileUtils.workingDirectory.absolute }.")) {
		args =>
			// Determines the project root path
			val rootPath = Paths.get(args("root").string.getOrElse {
				println(s"Please specify the path to the project root directory. \nThe path may be absolute or relative to ${
					FileUtils.workingDirectory.absolute }")
				StdIn.readLine()
			})
			if (rootPath.exists) {
				cPool.tryWith { implicit c =>
					ProjectActions.setup(rootPath) match {
						case Some(project) => println(s"Successfully set up ${ project.name }")
						case None => println("Project creation canceled")
					}
				}.logFailure
			}
	}
	
	/**
	 * Commands made available through this interface
	 */
	val commands = Single(setupProjectCommand)
	
	
	// IMPLICIT --------------------------------------
	
	implicit def autoAccessCommands(@unused o: ProjectCommands.type): Seq[Command] = commands
}