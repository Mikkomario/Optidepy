package vf.optidepy.util

import utopia.bunnymunch.jawn.JsonBunny
import utopia.flow.async.context.ThreadPool
import utopia.flow.parse.file.FileExtensions._
import utopia.flow.parse.json.JsonParser
import utopia.flow.util.logging.{Logger, SysErrLogger}

import java.nio.file.Path
import scala.concurrent.ExecutionContext

/**
 * Contains values common to the whole project
 * @author Mikko Hilpinen
 * @since 20.3.2023, v0.1
 */
object Common
{
	implicit val log: Logger = SysErrLogger
	implicit val exc: ExecutionContext = new ThreadPool("Optidepy").executionContext
	implicit val jsonParser: JsonParser = JsonBunny
	
	val dataDirectory: Path = "data"
}
