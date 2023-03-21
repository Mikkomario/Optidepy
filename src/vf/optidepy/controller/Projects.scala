package vf.optidepy.controller

import utopia.flow.parse.file.container.ObjectsFileContainer
import utopia.flow.parse.file.FileExtensions._
import vf.optidepy.model.DeployedProject
import vf.optidepy.util.Common._

/**
 * Used for interacting with active projects
 * @author Mikko Hilpinen
 * @since 20.3.2023, v0.1
 */
object Projects
{
	// ATTRIBUTES   --------------------------
	
	private val container = new ObjectsFileContainer(dataDirectory/"projects.json", DeployedProject)
	
	
	//
}
