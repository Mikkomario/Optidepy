package vf.optidepy.model.library

import utopia.flow.parse.string.Regex
import utopia.flow.parse.file.FileExtensions._
import vf.optidepy.model.library.Module.scalaJarRegex

import java.nio.file.Path

object Module
{
	private val scalaJarRegex = Regex("scala-library") + Regex.any + Regex.escape('.') + Regex("jar")
}

/**
 * Represents a project module
 * @author Mikko Hilpinen
 * @since Maverick v0.1 3.10.2021, added to Optidepy 9.4.2024
 */
case class Module(name: String, changeListPath: Path, artifactDirectory: Path)
{
	/**
	 * @return Whether this module exports full applications and not just individual jar files
	 */
	// TODO: Likely we need a new implementation for the application use-case since it overlaps with the deployment function
	// Checks whether the export directory contains a scala-library jar file
	lazy val isApplication = artifactDirectory
		.iterateChildren { _.exists { p => p.isRegularFile && scalaJarRegex(p.fileName) } }
		.getOrElse(false)
}
