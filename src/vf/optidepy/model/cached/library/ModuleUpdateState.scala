package vf.optidepy.model.cached.library

import utopia.flow.collection.immutable.Empty
import utopia.flow.util.Version
import vf.optidepy.model.combined.library.PossiblyReleasedProjectModule

import java.nio.file.Path

/**
 * Represents results of a module update scan.
 * Shows whether there are any changes present since the last release and locates the latest jar file, if present.
 * @param module Module that has possibly been updated. Includes the latest release, if available.
 * @param version Current / latest module version, typically based on the documentation
 * @param documentation Lines of text present in the documentation for this version. Default = empty.
 * @param jarPath Path to the jar file associated with this update. None if no jar was found.
 * @param jarIsVersioned Whether the 'jarPath' points to a versioned jar (true) or a non-versioned jar (false).
 *                       Versioned jars are typically associated with earlier releases,
 *                       while non-versioned jars are assumed to represent the module's current state.
 * @param isDevelopmentVersion Whether the module's current version has been marked as "in development",
 *                             i.e. not yet (fully) released.
 *                             Default = false = this update has (presumably) already been released.
 * @author Mikko Hilpinen
 * @since 24.08.2024, v1.2
 */
case class ModuleUpdateState(module: PossiblyReleasedProjectModule, version: Version,
                             documentation: Seq[String] = Empty, jarPath: Option[Path] = None,
                             jarIsVersioned: Boolean = false, isDevelopmentVersion: Boolean = false)
