package vf.optidepy.model.library

import utopia.flow.view.template.Extender

/**
 * Attaches released module versions to a library module
 *
 * @author Mikko Hilpinen
 * @since 15.04.2024, v1.2
 */
case class VersionedModuleWithReleases(module: VersionedModule, releases: Vector[ModuleRelease])
	extends Extender[VersionedModule]
{
	override def wrapped: VersionedModule = module
}
