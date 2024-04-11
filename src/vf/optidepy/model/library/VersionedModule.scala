package vf.optidepy.model.library

import utopia.flow.parse.string.Regex
import utopia.flow.parse.file.FileExtensions._
import vf.optidepy.model.library.VersionedModule.scalaJarRegex

import java.nio.file.Path

object VersionedModule
{
	private val scalaJarRegex = Regex("scala-library") + Regex.any + Regex.escape('.') + Regex("jar")
}

/**
 * Represents a project module where versions and changes are tracked and documented
 * @author Mikko Hilpinen
 * @since Maverick v0.1 3.10.2021, added to Optidepy 9.4.2024
 */
// TODO: Add an id property
case class VersionedModule(name: String, changeListPath: Path, artifactDirectory: Path)
{
	/**
	 * @return Whether this module exports full applications and not just individual jar files
	 */
	// Checks whether the export directory contains a scala-library jar file
	@deprecated("Deprecated for removal. Application deployments should be handled using ProjectDeploymentConfig.")
	lazy val isApplication = artifactDirectory
		.iterateChildren { _.exists { p => p.isRegularFile && scalaJarRegex(p.fileName) } }
		.getOrElse(false)
}
