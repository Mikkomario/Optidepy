package vf.optidepy.model.dependency

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.factory.FromModelFactoryWithSchema
import utopia.flow.generic.model.immutable.{Model, ModelDeclaration}
import utopia.flow.generic.model.mutable.DataType.StringType
import utopia.flow.generic.model.template.ModelConvertible
import utopia.flow.parse.file.FileExtensions._

import java.nio.file.Path

object ModuleDependency extends FromModelFactoryWithSchema[ModuleDependency]
{
	override lazy val schema: ModelDeclaration =
		ModelDeclaration("moduleId" -> StringType, "jarDirectory" -> StringType)
	
	override protected def fromValidatedModel(model: Model): ModuleDependency =
		apply(model("moduleId").getString, model("jarDirectory").getString,
			model("libraryFile").string.map { s => s: Path })
}

/**
 * Represents an application's dependency on a single versioned library module
 * @param moduleId Id of the module on which the described application is dependent upon
 * @param relativeJarDirectory Path relative to the application root directory,
 *                             which points to the directory where module jars are stored
 * @param relativeLibraryFilePath Path relative to the application root directory,
 *                                which specifies which jar is loaded as a library into the project.
 *                                None if no such file is used.
 * @author Mikko Hilpinen
 * @since 10.04.2024, v1.2
 */
case class ModuleDependency(moduleId: String, relativeJarDirectory: Path,
                            relativeLibraryFilePath: Option[Path] = None)
	extends ModelConvertible
{
	override def toModel: Model = Model.from(
		"moduleId" -> moduleId,
		"jarDirectory" -> relativeJarDirectory.toJson,
		"libraryFile" -> relativeLibraryFilePath.map { _.toJson })
}