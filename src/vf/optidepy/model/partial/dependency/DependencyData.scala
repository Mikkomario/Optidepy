package vf.optidepy.model.partial.dependency

import utopia.flow.collection.immutable.Single
import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.factory.FromModelFactoryWithSchema
import utopia.flow.generic.model.immutable.{Model, ModelDeclaration, PropertyDeclaration}
import utopia.flow.generic.model.mutable.DataType.{InstantType, IntType, StringType}
import utopia.flow.generic.model.template.ModelConvertible
import utopia.flow.parse.file.FileExtensions._
import utopia.flow.time.Now
import vf.optidepy.model.factory.dependency.DependencyFactory

import java.nio.file.Path
import java.time.Instant

object DependencyData extends FromModelFactoryWithSchema[DependencyData]
{
	// ATTRIBUTES	--------------------
	
	override lazy val schema = ModelDeclaration(Vector(
		PropertyDeclaration("dependentProjectId", IntType, Single("dependent_project_id")),
		PropertyDeclaration("usedModuleId", IntType, Single("used_module_id")),
		PropertyDeclaration("relativeLibDirectory", StringType, Single("relative_lib_directory")),
		PropertyDeclaration("libraryFileName", StringType, Single("library_file_name"), isOptional = true),
		PropertyDeclaration("created", InstantType, isOptional = true),
		PropertyDeclaration("deprecatedAfter", InstantType, Single("deprecated_after"), isOptional = true)))
	
	
	// IMPLEMENTED	--------------------
	
	override protected def fromValidatedModel(valid: Model) = DependencyData(
		valid("dependentProjectId").getInt, valid("usedModuleId").getInt,
		valid("relativeLibDirectory").getString: Path, valid("libraryFileName").getString,
		valid("created").getInstant, valid("deprecatedAfter").instant)
}

/**
  * Represents a dependency of a project / module from a specific library
  * @param dependentProjectId Id of the project which this dependency concerns
  * @param usedModuleId Id of the module the referenced project uses
  * @param relativeLibDirectory Path to the directory where library jars will be placed.
 *                             Relative to the project's root directory.
  * @param libraryFileName Name of the library file matching this dependency. Empty if not applicable.
  * @param created Time when this dependency was registered
  * @param deprecatedAfter Time when this dependency was replaced or removed. None while active.
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DependencyData(dependentProjectId: Int, usedModuleId: Int, relativeLibDirectory: Path,
                          libraryFileName: String = "", created: Instant = Now, deprecatedAfter: Option[Instant] = None)
	extends DependencyFactory[DependencyData] with ModelConvertible
{
	// COMPUTED	--------------------
	
	/**
	  * Whether this dependency has already been deprecated
	  */
	def isDeprecated = deprecatedAfter.isDefined
	/**
	  * Whether this dependency is still valid (not deprecated)
	  */
	def isValid = !isDeprecated
	
	
	// IMPLEMENTED	--------------------
	
	override def toModel = Model(Vector(
		"dependentProjectId" -> dependentProjectId, "usedModuleId" -> usedModuleId,
		"relativeLibDirectory" -> relativeLibDirectory.toJson, "libraryFileName" -> libraryFileName,
		"created" -> created, "deprecatedAfter" -> deprecatedAfter))
	
	override def withCreated(created: Instant) = copy(created = created)
	override def withDependentProjectId(dependentProjectId: Int) = copy(dependentProjectId = dependentProjectId)
	override def withDeprecatedAfter(deprecatedAfter: Instant) = copy(deprecatedAfter = Some(deprecatedAfter))
	override def withLibraryFileName(libraryFileName: String) = copy(libraryFileName = libraryFileName)
	override def withRelativeLibDirectory(relativeLibDirectory: Path) = 
		copy(relativeLibDirectory = relativeLibDirectory)
	override def withUsedModuleId(usedModuleId: Int) = copy(usedModuleId = usedModuleId)
}

