package vf.optidepy.model.library

import utopia.flow.view.template.Extender
import vf.optidepy.model.enumeration.UpdateType

/**
 * Represents a module update
 *
 * @author Mikko Hilpinen
 * @since 3.10.2021, v0.1
 * @param newState Module version after update
 * @param changeDocLines Lines describing changes introduced in this update
 */
@deprecated("This model will likely get removed", "v1.2")
case class ModuleUpdate(newState: ModuleVersionExport, changeDocLines: Vector[String])
	extends Extender[ModuleVersionExport]
{
	// ATTRIBUTES   -----------------------------
	
	/**
	 * How big this update is / type of this update
	 */
	lazy val updateType = UpdateType.from(newState.version)
	
	
	// IMPLEMENTED  -----------------------------
	
	override def wrapped = newState
}

