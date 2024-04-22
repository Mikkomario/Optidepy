package vf.optidepy.model.library

import utopia.flow.generic.model.immutable.{Model, ModelDeclaration}
import utopia.flow.generic.model.template.ModelConvertible
import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.factory.FromModelFactoryWithSchema
import utopia.flow.generic.model.mutable.DataType.StringType
import utopia.flow.parse.string.Regex
import utopia.flow.parse.file.FileExtensions._
import vf.optidepy.model.library.VersionedModule.scalaJarRegex

import java.nio.file.Path

object VersionedModule extends FromModelFactoryWithSchema[VersionedModule]
{
	// ATTRIBUTES   -----------------------
	
	private val scalaJarRegex = Regex("scala-library") + Regex.any + Regex.escape('.') + Regex("jar")
	
	override lazy val schema: ModelDeclaration = ModelDeclaration(
		"id" -> StringType, "name" -> StringType, "changeList" -> StringType, "artifactDir" -> StringType)
	
	
	// IMPLEMENTED  -----------------------
	
	override protected def fromValidatedModel(model: Model): VersionedModule =
		apply(model("id").getString, model("name").getString, model("changeList").getString,
			model("artifactDir").getString)
}

/**
 * Represents a project module where versions and changes are tracked and documented
 * @author Mikko Hilpinen
 * @since Maverick v0.1 3.10.2021, added to Optidepy 9.4.2024
 */
case class VersionedModule(id: String, name: String, changeListPath: Path, artifactDirectory: Path)
	extends ModelConvertible
{
	// ATTRIBUTES   -------------------------
	
	/**
	 * @return Whether this module exports full applications and not just individual jar files
	 */
	// Checks whether the export directory contains a scala-library jar file
	@deprecated("Deprecated for removal. Application deployments should be handled using ProjectDeploymentConfig.")
	lazy val isApplication = artifactDirectory
		.iterateChildren { _.exists { p => p.isRegularFile && scalaJarRegex(p.fileName) } }
		.getOrElse(false)
	
	
	// IMPLEMENTED  -------------------------
	
	override def toModel: Model = Model.from(
		"id" -> id, "name" -> name, "changeList" -> changeListPath.toJson, "artifactDir" -> artifactDirectory.toJson)
}
