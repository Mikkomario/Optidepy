package vf.optidepy.model.partial.library

import utopia.flow.collection.immutable.Single
import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.factory.FromModelFactoryWithSchema
import utopia.flow.generic.model.immutable.{Model, ModelDeclaration, PropertyDeclaration}
import utopia.flow.generic.model.mutable.DataType.InstantType
import utopia.flow.generic.model.mutable.DataType.IntType
import utopia.flow.generic.model.mutable.DataType.StringType
import utopia.flow.generic.model.template.ModelConvertible
import utopia.flow.parse.file.FileExtensions._
import utopia.flow.time.Now
import vf.optidepy.model.factory.library.VersionedModuleFactory

import java.nio.file.Path
import java.time.Instant

object VersionedModuleData extends FromModelFactoryWithSchema[VersionedModuleData]
{
	// ATTRIBUTES	--------------------
	
	override lazy val schema = 
		ModelDeclaration(Vector(PropertyDeclaration("projectId", IntType, Single("project_id")), 
			PropertyDeclaration("name", StringType), PropertyDeclaration("relativeChangeListPath", 
			StringType, Single("relative_change_list_path")), 
			PropertyDeclaration("relativeArtifactDirectory", StringType, 
			Single("relative_artifact_directory")), PropertyDeclaration("created", InstantType, 
			isOptional = true), PropertyDeclaration("deprecatedAfter", InstantType, 
			Single("deprecated_after"), isOptional = true)))
	
	
	// IMPLEMENTED	--------------------
	
	override protected def fromValidatedModel(valid: Model) = 
		VersionedModuleData(valid("projectId").getInt, valid("name").getString, 
			valid("relativeChangeListPath").getString: Path, 
			valid("relativeArtifactDirectory").getString: Path, valid("created").getInstant, 
			valid("deprecatedAfter").instant)
}

/**
  * Represents a library module which may be exported or added as a dependency to another project
  * @param projectId Id of the project this module is part of
  * @param name Name of this module
  * @param relativeChangeListPath A path relative to the project root directory, 
  * which points to this module's Changes.md file
  * @param relativeArtifactDirectory A path relative to the project root directory, 
  * which points to the directory where artifact jar files will be exported
  * @param created Time when this module was introduced
  * @param deprecatedAfter Time when this module was archived or removed
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class VersionedModuleData(projectId: Int, name: String, relativeChangeListPath: Path, 
	relativeArtifactDirectory: Path, created: Instant = Now, deprecatedAfter: Option[Instant] = None) 
	extends VersionedModuleFactory[VersionedModuleData] with ModelConvertible
{
	// COMPUTED	--------------------
	
	/**
	  * Whether this versioned module has already been deprecated
	  */
	def isDeprecated = deprecatedAfter.isDefined
	
	/**
	  * Whether this versioned module is still valid (not deprecated)
	  */
	def isValid = !isDeprecated
	
	
	// IMPLEMENTED	--------------------
	
	override def toModel = 
		Model(Vector("projectId" -> projectId, "name" -> name, 
			"relativeChangeListPath" -> relativeChangeListPath.toJson, 
			"relativeArtifactDirectory" -> relativeArtifactDirectory.toJson, "created" -> created, 
			"deprecatedAfter" -> deprecatedAfter))
	
	override def withCreated(created: Instant) = copy(created = created)
	
	override def withDeprecatedAfter(deprecatedAfter: Instant) = copy(deprecatedAfter = Some(deprecatedAfter))
	
	override def withName(name: String) = copy(name = name)
	
	override def withProjectId(projectId: Int) = copy(projectId = projectId)
	
	override def withRelativeArtifactDirectory(relativeArtifactDirectory: Path) = 
		copy(relativeArtifactDirectory = relativeArtifactDirectory)
	
	override def withRelativeChangeListPath(relativeChangeListPath: Path) = 
		copy(relativeChangeListPath = relativeChangeListPath)
}

