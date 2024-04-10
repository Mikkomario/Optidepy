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
// TODO: Rename to ModuleVersionUpdate
case class ModuleUpdate(newState: ModuleExport, changeDocLines: Vector[String]) extends Extender[ModuleExport]
{
	// ATTRIBUTES   -----------------------------
	
	/**
	 * How big this update is / type of this update
	 */
	lazy val updateType = UpdateType.from(newState.version)
	
	
	// IMPLEMENTED  -----------------------------
	
	override def wrapped = newState
}

