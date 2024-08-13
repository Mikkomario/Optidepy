package vf.optidepy.controller.dependency

import utopia.flow.collection.CollectionExtensions._
import utopia.flow.parse.file.FileExtensions._
import utopia.flow.parse.string.IterateLines
import utopia.flow.util.logging.Logger

import java.nio.file.Path

/**
 * Interface for identifying and interacting with IntelliJ files
 *
 * @author Mikko Hilpinen
 * @since 10.08.2024, v1.2
 */
object IdeaFiles
{
	// ATTRIBUTES   ----------------------
	
	private val ideaDirectoryName = ".idea"
	private val librariesDirName = "libraries"
	private val artifactsDirName = "artifacts"
	
	
	// OTHER    --------------------------
	
	/**
	 * Finds the .idea directory from within a project
	 * @param projectDirectory Project (root) directory
	 * @param log Implicit logging implementation to handle non-critical failures
	 * @return Located IDEA directory. None if not found.
	 */
	def locate(projectDirectory: Path)(implicit log: Logger) =
		projectDirectory.toTree.nodesBelowIteratorUpToDepth(2)
			.find { _.nav.fileName == ideaDirectoryName }.map { node => LocatedIdeaFiles(node.nav) }
	
	
	// NESTED   ---------------------------
	
	case class LocatedIdeaFiles(ideaDirectory: Path)
	{
		def libraries = ideaDirectory/librariesDirName
		def artifacts = ideaDirectory/artifactsDirName
		
		/**
		 * Finds a library file referring to the specified jar
		 * @param jarFileName Name of the searched jar file
		 * @param log Implicit logging implementation for handling file-read failures
		 * @return Path to the library file referring to the specified jar.
		 *         None if no such file was found.
		 */
		def locateLibraryReferringTo(jarFileName: String)(implicit log: Logger) =
			libraries
				.iterateChildren { iter =>
					iter.find { libraryFile =>
						IterateLines.fromPath(libraryFile) { _.exists { _.contains(jarFileName) } }.getOrElseLog(false)
					}
				}
				.getOrElseLog(None)
		
		/* NB: Also needs to update META-INF/MANIFEST.MF
		def updateArtifactJarReferences(previousJarName: String, newJarName: String,
		                                backupDirectory: Option[Path]) =
		{
		
		}*/
	}
}
