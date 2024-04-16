package vf.optidepy.model

import vf.optidepy.model.dependency.ModuleDependency
import vf.optidepy.model.deployment.ProjectDeployments
import vf.optidepy.model.library.VersionedModuleWithReleases

import java.nio.file.Path

/**
 * Represents a project that may be deployed.
 * Immutable, but contains stateful information.
 *
 * @param name Name of this project
 * @param rootPath Path common to this project's deployment input (if applicable) and other relative paths
 * @param modules Versioned modules that are part of this project (library use-case)
 * @param deploymentConfig Configuration used when deploying this project (default use-case).
 *                         Contains deployment history.
 *                         None if not deployable (library use-case)
 * @param moduleDependencies List of dependencies on that library's modules
 * @param relativeArtifactsDirPath Path relative to the application root directory,
 *                                 which contains the project artifacts which describe
 *                                 how project code and dependencies are to be exported.
 *                                 None if not applicable to this project.
 * @author Mikko Hilpinen
 * @since 15.04.2024, v1.2
 */
case class Project(name: String, rootPath: Path, modules: Vector[VersionedModuleWithReleases] = Vector.empty,
                   deploymentConfig: Option[ProjectDeployments] = None,
                   moduleDependencies: Vector[ModuleDependency] = Vector.empty,
                   relativeArtifactsDirPath: Option[Path] = None)

