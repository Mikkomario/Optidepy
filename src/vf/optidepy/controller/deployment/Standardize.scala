package vf.optidepy.controller.deployment

import utopia.flow.async.context.ActionQueue
import utopia.flow.collection.CollectionExtensions._
import utopia.flow.parse.file.FileExtensions._
import utopia.flow.parse.string.Regex
import utopia.flow.util.StringExtensions._

import java.nio.file.Path
import scala.collection.immutable.VectorBuilder
import scala.collection.mutable
import scala.concurrent.ExecutionContext
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
	// TODO: Add prompts / queueing
	def fileNamesUnder(root: Path)(implicit exc: ExecutionContext) = {
		val failuresBuilder = new VectorBuilder[(Path, Throwable, String)]()
		val actionQueue = new ActionQueue(20)
		// Processes file names recursively
		_fileNamesUnder(root, failuresBuilder, actionQueue)
		
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
	                            actionQueue: ActionQueue): Unit =
	{
		dir.children match {
			case Success(children) =>
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
				val renamed = newNames.groupBy { _._2 }.flatMap { case (fileName, paths) =>
					paths.oneOrMany match {
						case Left((path, _, shouldRename)) =>
							if (shouldRename)
								Some(rename(path, fileName, failuresBuilder))
							else
								Some(path)
						case Right(conflicts) =>
							conflicts.zipWithIndex.map { case ((path, _, _), i) =>
								rename(path, s"$fileName-$i", failuresBuilder)
							}
					}
				}
				// Proceeds to the sub-directories using recursion and multi-threading
				val asyncProcesses = renamed.filter { _.isDirectory }.map { subDirectory =>
					val action = actionQueue.push { _fileNamesUnder(subDirectory, failuresBuilder, actionQueue) }
					action.waitUntilStarted()
					(subDirectory, action)
				}
				// Blocks until all asynchronous processes have completed
				asyncProcesses.foreach { case (dir, action) =>
					action.waitFor().failure.foreach { error =>
						failuresBuilder += ((dir, error, s"Failed to process files under $dir"))
					}
				}
			
			case Failure(error) => failuresBuilder += ((dir, error, s"Failed to iterate children under $dir"))
		}
	}
	
	private def rename(path: Path, to: String, failuresBuilder: mutable.Growable[(Path, Throwable, String)]) = {
		path.rename(to).getOrMap { error =>
			failuresBuilder += ((path, error, s"Failed to rename $path to $to"))
			path
		}
	}
}
