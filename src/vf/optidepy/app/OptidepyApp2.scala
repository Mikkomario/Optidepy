package vf.optidepy.app

import utopia.flow.util.console.Console
import vf.optidepy.app.command.ProjectCommands
import vf.optidepy.util.Common._

/**
 * The main interface to the Optidepy application
 *
 * @author Mikko Hilpinen
 * @since 23.08.2024, v1.2
 */
object OptidepyApp2
{
	// ATTRIBUTES   ------------------------------
	
	private val console = Console.static(ProjectCommands, "\nNext command", "exit")
	
	
	// APP CODE ----------------------------------
	
	console.run()
	println("Bye!")
}
