package vf.optidepy.test

import utopia.flow.collection.immutable.Pair
import utopia.flow.operator.equality.EqualsFunction
import utopia.flow.parse.file.FileExtensions._

import java.nio.file.Path

/**
 * Tests jar file lastModified access
 * @author Mikko Hilpinen
 * @since 20.3.2023, v0.1
 */
object JarModifiedTest extends App
{
	implicit val iteratorEquals: EqualsFunction[Iterator[Any]] = _ sameElements _
	
	val path: Path = "C:/Users/Mikkomario.SMAUG/IdeaProjects/Fuel/out/artifacts/Fuel_Client_Front_jar/Dependencies.jar"
	val target: Path = "C:/Users/Mikkomario.SMAUG/Fuel/Client/full/Dependencies.jar"
	//"out/artifacts/Optidepy/Optidepy.jar"
	
	println(s"Jar exists: ${ path.exists }")
	println(s"Jar not exists: ${ path.notExists }")
	println(s"Jar last modified: ${ path.lastModified }")
	
	val areDifferent = path.tryReadWith { sourceStream =>
		target.readWith { targetStream =>
			Pair(sourceStream, targetStream)
				.map { stream => Iterator.continually { stream.read() }.take(100000).takeWhile { _ >= 0 } }
				.isAsymmetric
		}
	}.get
	println(s"Different contents: $areDifferent")
}
