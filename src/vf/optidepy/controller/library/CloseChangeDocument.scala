package vf.optidepy.controller.library

import utopia.flow.parse.string.Regex
import utopia.flow.parse.file.FileExtensions._
import utopia.flow.time.Today
import utopia.flow.util.Version
import vf.optidepy.model.library.VersionedModule

import java.time.format.DateTimeFormatter
import scala.io.Codec

/**
 * Used for "closing" the development version in module change documents
 * @author Mikko Hilpinen
 * @since 4.10.2021, v0.1
 */
object CloseChangeDocument
{
	// ATTRIBUTES   ----------------------------
	
	/**
	 * Format used when writing version release date
	 */
	val dateFormat = DateTimeFormatter.ofPattern("dd.MM.uuuu")
	
	private val developmentVersionLineRegex = Regex.escape('#') + Regex.any + Version.regex +
		Regex.any + Regex("dev") + Regex.any
	
	
	// OTHER    --------------------------------
	
	/**
	 * Closes the change list document of the specified module
	 * @param module Targeted module
	 * @return Path to the edited change list document. Failure if change list editing failed.
	 */
	def apply(module: VersionedModule, summaryLines: Seq[String] = Vector())(implicit codec: Codec) =
		module.changeListPath.edit { editor =>
			// Finds the development version line and overwrites it
			editor.flatMapNextWhere(developmentVersionLineRegex.apply) { original =>
				// Replaces the part after the version number with a release date
				// May also add extra summary lines
				val versionEndIndex = Version.regex.endIndexIteratorIn(original).next()
				val newHeader = original.take(versionEndIndex) + " - " + Today.toLocalDate.format(dateFormat)
				newHeader +: summaryLines
			}
		}
}
