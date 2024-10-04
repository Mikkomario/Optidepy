package vf.optidepy.model

import utopia.flow.collection.CollectionExtensions._
import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.factory.FromModelFactoryWithSchema
import utopia.flow.generic.model.immutable.{Model, ModelDeclaration}
import utopia.flow.generic.model.mutable.DataType.StringType
import utopia.flow.generic.model.template.ModelConvertible
import utopia.flow.parse.file.FileExtensions._
import utopia.flow.util.TryExtensions._
import vf.optidepy.model.dependency.ModuleDependency
import vf.optidepy.model.deployment.ProjectDeployments
import vf.optidepy.model.library.VersionedModuleWithReleases
import vf.optidepy.util.Common._

import java.nio.file.Path

@deprecated("Will be replaced with a new version", "v1.2")
object Project extends FromModelFactoryWithSchema[Project]
{
	// ATTRIBUTES   ----------------------------
	
	override lazy val schema = ModelDeclaration("name" -> StringType, "root" -> StringType)
	
	
	// IMPLEMENTED  ----------------------------
	
	override protected def fromValidatedModel(model: Model): Project = {
		val modules = model("modules").vector match {
			case Some(modules) =>
				modules.tryMap { v => VersionedModuleWithReleases(v.getModel) }.getOrElseLog { Vector.empty }
			case None => Vector.empty
		}
		val deployment = model("deployment").model.flatMap { ProjectDeployments(_).logToOption }
		val dependencies = model("dependencies").vector match {
			case Some(dependencies) =>
				dependencies.tryMap { v => ModuleDependency(v.getModel) }.getOrElseLog { Vector.empty }
			case None => Vector.empty
		}
		
		apply(model("name").getString, model("root").getString, modules, deployment, dependencies,
			model("artifactsDir").string.map { s => s: Path })
	}
}

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
@deprecated("Will be replaced with a new version", "v1.2")
case class Project(name: String, rootPath: Path, modules: Vector[VersionedModuleWithReleases] = Vector.empty,
                   deploymentConfig: Option[ProjectDeployments] = None,
                   moduleDependencies: Vector[ModuleDependency] = Vector.empty,
                   relativeArtifactsDirPath: Option[Path] = None)
	extends ModelConvertible
{
	override def toModel: Model = Model.from(
		"name" -> name, "root" -> rootPath.toJson,
		"modules" -> modules, "deployment" -> deploymentConfig, "dependencies" -> moduleDependencies,
		"artifactsDir" -> relativeArtifactsDirPath.map { _.toJson })
}
