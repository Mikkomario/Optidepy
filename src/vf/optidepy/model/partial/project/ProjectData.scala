package vf.optidepy.model.partial.project

import utopia.flow.collection.immutable.Single
import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.factory.FromModelFactoryWithSchema
import utopia.flow.generic.model.immutable.{Model, ModelDeclaration, PropertyDeclaration, Value}
import utopia.flow.generic.model.mutable.DataType.InstantType
import utopia.flow.generic.model.mutable.DataType.StringType
import utopia.flow.generic.model.template.ModelConvertible
import utopia.flow.parse.file.FileExtensions._
import utopia.flow.time.Now
import vf.optidepy.model.factory.project.ProjectFactory

import java.nio.file.Path
import java.time.Instant

object ProjectData extends FromModelFactoryWithSchema[ProjectData]
{
	// ATTRIBUTES	--------------------
	
	override lazy val schema = 
		ModelDeclaration(Vector(PropertyDeclaration("name", StringType), PropertyDeclaration("rootPath", 
			StringType, Single("root_path")), PropertyDeclaration("relativeIdeaPath", StringType, 
			Single("relative_idea_path"), isOptional = true), PropertyDeclaration("created", InstantType, 
			isOptional = true), PropertyDeclaration("deprecatedAfter", InstantType, 
			Single("deprecated_after"), isOptional = true)))
	
	
	// IMPLEMENTED	--------------------
	
	override protected def fromValidatedModel(valid: Model) = 
		ProjectData(valid("name").getString, valid("rootPath").getString: Path, 
			Some(valid("relativeIdeaPath").getString: Path), valid("created").getInstant, 
			valid("deprecatedAfter").instant)
}

/**
  * Represents a project that may be deployed, released or updated
  * @param name Name of this project
  * @param rootPath Path to the directory that contains this project
  * @param relativeIdeaPath Path to the .idea directory, 
	if applicable. Relative to this project's root directory.
  * @param created Time when this project was introduced
  * @param deprecatedAfter Time whe this project was removed / archived
  * @author Mikko Hilpinen
  * @since 12.08.2024, v1.2
  */
case class ProjectData(name: String, rootPath: Path, relativeIdeaPath: Option[Path] = None, 
	created: Instant = Now, deprecatedAfter: Option[Instant] = None) 
	extends ProjectFactory[ProjectData] with ModelConvertible
{
	// COMPUTED	--------------------
	
	/**
	  * Whether this project has already been deprecated
	  */
	def isDeprecated = deprecatedAfter.isDefined
	
	/**
	  * Whether this project is still valid (not deprecated)
	  */
	def isValid = !isDeprecated
	
	
	// IMPLEMENTED	--------------------
	
	override def toModel = 
		Model(Vector("name" -> name, "rootPath" -> rootPath.toJson, 
			"relativeIdeaPath" -> (relativeIdeaPath match { case Some(v) => v.toJson; case None => Value.empty }), 
			"created" -> created, "deprecatedAfter" -> deprecatedAfter))
	
	override def withCreated(created: Instant) = copy(created = created)
	
	override def withDeprecatedAfter(deprecatedAfter: Instant) = copy(deprecatedAfter = Some(deprecatedAfter))
	
	override def withName(name: String) = copy(name = name)
	
	override
		 def withRelativeIdeaPath(relativeIdeaPath: Path) = copy(relativeIdeaPath = Some(relativeIdeaPath))
	
	override def withRootPath(rootPath: Path) = copy(rootPath = rootPath)
}

