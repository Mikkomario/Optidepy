package vf.optidepy.model.combined.project

import vf.optidepy.model.combined.deployment.DeploymentConfigWithBindings
import vf.optidepy.model.stored.dependency.Dependency
import vf.optidepy.model.stored.library.VersionedModule
import vf.optidepy.model.stored.project.Project

/**
 * Combines a project with its rarely changing information, namely:
 *      1. Its versioned modules
 *      1. Its deployment configurations
 *      1. Its dependencies from other projects
 *
 * @author Mikko Hilpinen
 * @since 23.08.2024, v1.2
 */
case class DetailedProject(project: Project, modules: Seq[VersionedModule],
                           deploymentConfigs: Seq[DeploymentConfigWithBindings], dependencies: Seq[Dependency])
	extends ProjectWithModules with CombinedProject[DetailedProject]
{
	override protected def wrap(factory: Project): DetailedProject = copy(project = factory)
}