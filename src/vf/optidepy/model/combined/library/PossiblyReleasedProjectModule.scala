package vf.optidepy.model.combined.library

import vf.optidepy.model.stored.library.{ModuleRelease, VersionedModule}
import vf.optidepy.model.stored.project.Project

/**
 * Combines versioned module, project and possible individual release information
 * @param module Wrapped module
 * @param project Project which this module is part of
 * @param release (latest) release of this module, if available
 * @author Mikko Hilpinen
 * @since 24.08.2024, v1.2
 */
case class PossiblyReleasedProjectModule(module: VersionedModule, project: Project,
                                         release: Option[ModuleRelease] = None)
	extends VersionedProjectModule with PossiblyReleasedModule
		with CombinedVersionedModule[PossiblyReleasedProjectModule]
{
	// IMPLEMENTED  --------------------
	
	override protected def wrap(factory: VersionedModule): PossiblyReleasedProjectModule = copy(module = factory)
}