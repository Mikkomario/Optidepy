package vf.optidepy.controller.library

import utopia.flow.collection.CollectionExtensions._
import utopia.flow.collection.immutable.Empty
import utopia.flow.parse.file.FileExtensions._
import utopia.flow.parse.string.{IterateLines, Regex}
import utopia.flow.time.Today
import utopia.flow.util.StringExtensions._
import utopia.flow.util.Version
import utopia.vault.database.Connection
import vf.optidepy.database.access.many.library.module.DbVersionedModules
import vf.optidepy.database.access.many.library.module.release.DbModuleReleases
import vf.optidepy.model.cached.library.ModuleUpdateState
import vf.optidepy.model.combined.library.PossiblyReleasedProjectModule
import vf.optidepy.model.library.{ModuleUpdate, ModuleVersionExport, VersionedModule}
import vf.optidepy.model.stored.project.Project

/**
 * Checks for module-specific updates
 * @author Mikko Hilpinen
 * @since 3.10.2021, v0.1
 */
object FindModuleUpdates
{
	// ATTRIBUTES   -----------------------
	
	private val versionLineRegex = Regex.escape('#') + Regex.any + Version.regex + Regex.any
	
	
	// OTHER    ---------------------------
	
	def apply(project: Project)(implicit connection: Connection) = {
		// Finds the modules which belong to this project
		val modules = DbVersionedModules.inProject(project.id).pull
		// Finds the latest release for each of these modules
		val projectModules = modules.map { module =>
			// TODO: Continue implementation
			???
			// DbModuleReleases.ofModule(module.id).latest
		}
	}
	
	/**
	 * Finds the current state of a module, if possible
	 * @param module Targeted module
	 * @return Current update state of that module.
	 *         Failure if failed to process the documentation, or if failed to access the artifacts.
	 */
	def apply(module: PossiblyReleasedProjectModule) = {
		// Scans the changes file for the latest version
		IterateLines
			.fromPath(module.changeListPath) { linesIter =>
				linesIter.nextWhere { versionLineRegex(_) }
					.map { versionLine =>
						val version = Version.findFrom(versionLine).getOrElse(Version(1))
						// Checks whether this is a development version or an existing version
						val isDevelopmentVersion = versionLine.containsIgnoreCase("dev")
						val docLines = linesIter.takeWhile { !versionLineRegex(_) }.toOptimizedSeq
							.dropRightWhile { _.isEmpty }
						
						(version, isDevelopmentVersion, docLines)
					}
					.toTry {
						new NoSuchElementException(
							s"${module.changeListPath.fileName} of ${module.name} doesn't list any module versions")
					}
			}
			.flatten
			.flatMap { case (version, isDevelopmentVersion, docLines) =>
				// Next attempts to find the jar file associated with this version
				// Starts by looking for a jar with no version information
				module.artifactDirectory.iterateChildren { _.filter { _.fileType == "jar" }.toOptimizedSeq }
					.map { jarFiles =>
						val (jar, jarIsVersioned) = jarFiles
							.find { jar => !Version.regex.existsIn(jar.fileName) } match
						{
							// Case: Non-versioned jar found => Connects that
							case Some(nonVersionedJar) => Some(nonVersionedJar) -> false
							// Case: No non-versioned jar found => Looks for a jar matching the current version
							case None =>
								val versionStr = version.toString
								val versionedJar = jarFiles.find { _.fileName.contains(versionStr) }
								versionedJar -> versionedJar.isDefined
						}
						
						// Combines and returns the information
						ModuleUpdateState(module, version, docLines, jar, jarIsVersioned, isDevelopmentVersion)
					}
			}
	}
	
	/**
	 * Finds the latest version or an update of the specified module
	 * @param module Module for which updates are searched
	 * @return Either Left: Latest module export or Right: Pending module update.
	 *         Failure if no version data was found or if no proper jar files were found.
	 */
	@deprecated("This version uses the old models and will be removed once the new model structure is in use", "v1.2")
	def apply(module: VersionedModule) = {
		// Reads the change list document and finds the latest version (line / range)
		IterateLines.fromPath(module.changeListPath) { linesIter =>
			linesIter.nextWhere { versionLineRegex(_) }
				.flatMap { line =>
					Version.findFrom(line).map { version =>
						// checks whether it's a development version or an existing version
						// Also counts today's releases as development versions (modified release use-case)
						// Case: A development version => checks for changes list and creates an update
						if (line.containsIgnoreCase("dev") ||
							line.contains(CloseChangeDocument.dateFormat.format(Today.toLocalDate)))
						{
							val changeLines = linesIter.takeWhile { !versionLineRegex(_) }.toVector
								.dropRightWhile { _.isEmpty }
							Right((version, changeLines))
						}
						// Case: An existing version, returns it
						else
							Left(version)
					}
				}
				// Fails if no version was listed
				.toTry { new NoSuchElementException(
					s"${module.changeListPath.fileName} of ${module.name} doesn't list any module versions") }
		}.flatten.flatMap { latest =>
			// Creates a module export based on found version. In case of an update, wraps the export in an update.
			ModuleVersionExport.findFor(module, latest.leftOrMap { _._1 }).map { export =>
				latest.mapBoth { _ => export } { case (_, changes) => ModuleUpdate(export, changes) }
			}
		}
	}
}
