package vf.optidepy.app.command

import utopia.flow.util.console.Command

import scala.language.implicitConversions

object Commands
{
	// IMPLICIT ---------------------------
	
	implicit def autoAccessCommands(c: Commands): Seq[Command] = c.commands
}

/**
 * Common trait for interfaces used for accessing interactive commands
 * @author Mikko Hilpinen
 * @since 24.08.2024, v1.2
 */
trait Commands
{
	// ABSTRACT --------------------------
	
	/**
	 * Commands made available through this interface
	 */
	def commands: Seq[Command]
}
