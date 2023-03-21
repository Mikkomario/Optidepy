package vf.optidepy.test

import utopia.flow.parse.file.FileExtensions._

import java.nio.file.Path

/**
 * Tests jar file lastModified access
 * @author Mikko Hilpinen
 * @since 20.3.2023, v0.1
 */
object JarModifiedTest extends App
{
	val path: Path = "C:/Users/Mikkomario.SMAUG/IdeaProjects/Fuel/out/artifacts/Fuel_Client_Front_jar/Dependencies.jar"
	//"out/artifacts/Optidepy/Optidepy.jar"
	
	println(s"Jar exists: ${ path.exists }")
	println(s"Jar not exists: ${ path.notExists }")
	println(s"Jar last modified: ${ path.lastModified }")
}
