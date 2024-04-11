package vf.optidepy.model.library

import utopia.flow.util.Version

object ModuleVersionExport
{
	/**
	 * Creates a module export based on a specific version number and module artifact files
	 * @param module Described module
	 * @param version Exported version
	 * @return A module export. Failure if exported module jars were not found.
	 */
	def findFor(module: VersionedModule, version: Version) =
		ArtifactJarPath.findFrom(module.artifactDirectory, version).map { apply(module, _) }
}

/**
 * Represents a specific (built) module version
 * @author Mikko Hilpinen
 * @since Maverick v0.1, 3.10.2021; Added to Optidepy 4.9.2024 at v1.2
 * @param module Module being updated
 */
case class ModuleVersionExport(module: VersionedModule, jarPath: ArtifactJarPath)
{
	/**
	 * @return Whether this module represents an application (not a single jar)
	 */
	@deprecated("Deprecated for removal. The application use-case should be covered by original Optidepy features.")
	def isApplication = false
	/**
	 * @return Whether this module export consists of a single jar
	 */
	@deprecated("Deprecated for removal. The application use-case should be covered by original Optidepy features.")
	def isJar = true
	
	/**
	 * @return Version of this export
	 */
	def version = jarPath.version
	/**
	 * @return Directory that stores the exported jars or application
	 */
	def artifactDirectory = module.artifactDirectory
}