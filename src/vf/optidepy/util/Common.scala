package vf.optidepy.util

import utopia.bunnymunch.jawn.JsonBunny
import utopia.flow.async.context.ThreadPool
import utopia.flow.parse.file.FileExtensions._
import utopia.flow.parse.json.JsonParser
import utopia.flow.util.logging.{Logger, SysErrLogger}
import utopia.vault.database.ConnectionPool

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
	implicit val exc: ExecutionContext = new ThreadPool("Optidepy")
	implicit val jsonParser: JsonParser = JsonBunny
	
	implicit val cPool: ConnectionPool = new ConnectionPool()
	
	val dataDirectory: Path = "data"
}
