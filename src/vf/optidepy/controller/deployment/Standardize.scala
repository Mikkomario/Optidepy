package vf.optidepy.controller.deployment

import utopia.flow.async.AsyncExtensions._
import utopia.flow.async.context.ActionQueue
import utopia.flow.collection.CollectionExtensions._
import utopia.flow.parse.file.FileExtensions._
import utopia.flow.parse.string.Regex
import utopia.flow.util.StringExtensions._
import utopia.flow.util.console.ConsoleExtensions._
import utopia.flow.view.mutable.Pointer
import utopia.flow.view.mutable.async.Volatile

import java.nio.file.Path
import scala.collection.immutable.VectorBuilder
import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}
import scala.io.StdIn
import scala.util.{Failure, Success}

/**
 * Used for standardizing file names (to facilitate deployment)
 *
 * @author Mikko Hilpinen
 * @since 07.05.2024, v1.2
 */
object Standardize
{
	// ATTRIBUTES   -------------------------
	
	private val maxFileNameLength = 255
	
	private val dashRegex = Regex.escape('-')
	private val acceptedCharRegex = (Regex.letterOrDigit || dashRegex).withinParenthesis
	private val acceptedFileNameRegex = acceptedCharRegex.oneOrMoreTimes
	private val multiDashRegex = dashRegex + dashRegex.oneOrMoreTimes
	
	
	// OTHER    -----------------------------
	
	/**
	 * Standardizes file names within the specified directory recursively
	 * @param root The directory within which renaming is performed
	 * @param exc Implicit execution context
	 */
	def fileNamesUnder(root: Path)(implicit exc: ExecutionContext) = {
		val failuresBuilder = new VectorBuilder[(Path, Throwable, String)]()
		val recursiveChoicesPointer = Volatile(Set[(Path, Boolean)]())
		// Processes file names recursively
		_fileNamesUnder(root, failuresBuilder, recursiveChoicesPointer, new ActionQueue(20), new ActionQueue())
			.waitFor()
		
		// Handles failures
		val failures = failuresBuilder.result()
		if (failures.nonEmpty) {
			failures.groupBy { _._2.getClass.getName }.foreach { case (failureName, failures) =>
				println(s"Encountered ${failures.size} $failureName errors")
				failures.head._2.printStackTrace()
			}
			println(s"Encountered the following failures")
			failures.map { case (path, _, message) => s"\t- $path: $message" }.sorted.foreach(println)
		}
	}
	
	private def _fileNamesUnder(dir: Path, failuresBuilder: mutable.Growable[(Path, Throwable, String)],
	                            recursiveChoicesPointer: Pointer[Set[(Path, Boolean)]],
	                            actionQueue: ActionQueue, queryQueue: ActionQueue)
	                           (implicit exc: ExecutionContext): Future[Unit] =
	{
		dir.children match {
			case Success(children) =>
				if (children.nonEmpty) {
					// Determines the correct file name for each path
					val newNames = children.map { path =>
						val fileName = path.fileName
						val (name, extension) = fileName.splitAtLast(".").toTuple
						if (acceptedFileNameRegex(name) && !multiDashRegex.existsIn(name) &&
							fileName.length <= maxFileNameLength)
							(path, fileName, false)
						else {
							val newName = name.replaceAllExcept(acceptedCharRegex, "-")
								.replaceEachMatchOf(multiDashRegex, "-")
								.dropWhile { _ == '-' }.dropRightWhile { _ == '-' }
								// May limit the file name length, also
								.take(maxFileNameLength - extension.length - 1)
							(path, s"$newName${extension.prependIfNotEmpty(".")}", true)
						}
					}
					// Performs the renames
					val (preserve, mayRename) = newNames.groupBy { _._2 }.flatDivideWith { case (fileName, paths) =>
						paths.oneOrMany match {
							case Left((path, _, shouldRename)) =>
								if (shouldRename)
									Some(Right(path -> fileName))
								else
									Some(Left(path))
							case Right(conflicts) =>
								conflicts.zipWithIndex.map { case ((path, _, _), i) =>
									Right(path -> s"$fileName-$i")
								}
						}
					}
					val renamed = {
						if (mayRename.nonEmpty)
							queryQueue
								.push { renameDirectoryContents(dir, mayRename, failuresBuilder, recursiveChoicesPointer) }
								.waitFor()
								.getOrElse { mayRename.map { _._1 } }
						else
							Vector()
					}
					// Proceeds to the sub-directories using recursion and multi-threading
					Future {
						val asyncProcesses = (preserve ++ renamed).filter { _.isDirectory }.map { subDirectory =>
							val action = actionQueue.push {
								_fileNamesUnder(subDirectory, failuresBuilder, recursiveChoicesPointer, actionQueue,
									queryQueue)
									.waitFor()
							}
							action.waitUntilStarted()
							(subDirectory, action)
						}
						// Blocks until all asynchronous processes have completed
						asyncProcesses.foreach { case (dir, action) =>
							action.waitFor().failure.foreach { error =>
								failuresBuilder += ((dir, error, s"Failed to process files under $dir"))
							}
						}
					}
				}
				else
					Future.unit
			
			case Failure(error) =>
				failuresBuilder += ((dir, error, s"Failed to iterate children under $dir"))
				Future.unit
		}
	}
	
	private def renameDirectoryContents(directory: Path, renames: Vector[(Path, String)],
	                                    failuresBuilder: mutable.Growable[(Path, Throwable, String)],
	                                    recursiveChoicesPointer: Pointer[Set[(Path, Boolean)]]) =
	{
		// Checks whether the choice to rename or not was made earlier already
		recursiveChoicesPointer.value.find { case (dir, _) => directory.isChildOf(dir) } match {
			// Case: Choice to rename was made earlier already
			case Some((_, choice)) =>
				if (choice)
					renames.map { case (path, to) => rename(path, to, failuresBuilder) }
				else
					renames.map { _._1 }
			case None =>
				renames.oneOrMany match {
					case Left((path, newName)) =>
						promptRenaming(s"Do you want to rename ${ path.fileName } to $newName in $directory?",
							renames, directory, failuresBuilder, recursiveChoicesPointer)
					case Right(renames) =>
						println(s"$directory:")
						renames.foreach { case (path, to) => println(s"\t- ${ path.fileName } => $to") }
						promptRenaming(s"\nPerform these ${ renames.size } renames?", renames, directory,
							failuresBuilder, recursiveChoicesPointer)
				}
		}
	}
	
	private def promptRenaming(prompt: String, renames: Iterable[(Path, String)], dir: Path,
	                           failuresBuilder: mutable.Growable[(Path, Throwable, String)],
	                           recursiveChoicesPointer: Pointer[Set[(Path, Boolean)]]) =
	{
		println(prompt)
		println(s"\t- [Y]es (default)\n\t- [N]o\n\t- [A]ll in subdirectories, also\n\t- N[O]ne here nor in subdirectories")
		if (renames.hasSize > 1)
			println(s"\t- Choose [I]ndividually for each file")
		StdIn.readLine().toLowerCase match {
			case "no" | "n" => renames.map { _._1 }
			case "all" | "a" =>
				recursiveChoicesPointer.value += (dir -> true)
				renames.map { case (path, to) => rename(path, to, failuresBuilder) }
			case "none" | "o" =>
				recursiveChoicesPointer.value += (dir -> false)
				renames.map { _._1 }
			case "i" =>
				renames.map { case (path, to) =>
					if (StdIn.ask(s"Do you want to rename ${ path.fileName } to $to?", default = true))
						rename(path, to, failuresBuilder)
					else
						path
				}
			case _ =>
				renames.map { case (path, to) => rename(path, to, failuresBuilder) }
		}
	}
	
	private def rename(path: Path, to: String, failuresBuilder: mutable.Growable[(Path, Throwable, String)]) = {
		path.rename(to).getOrMap { error =>
			failuresBuilder += ((path, error, s"Failed to rename $path to $to"))
			path
		}
	}
}
