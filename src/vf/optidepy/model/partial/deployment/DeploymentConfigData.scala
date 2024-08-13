package vf.optidepy.model.partial.deployment

import utopia.flow.collection.immutable.Single
import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.factory.FromModelFactoryWithSchema
import utopia.flow.generic.model.immutable.{Model, ModelDeclaration, PropertyDeclaration}
import utopia.flow.generic.model.mutable.DataType.BooleanType
import utopia.flow.generic.model.mutable.DataType.InstantType
import utopia.flow.generic.model.mutable.DataType.IntType
import utopia.flow.generic.model.mutable.DataType.StringType
import utopia.flow.generic.model.template.ModelConvertible
import utopia.flow.parse.file.FileExtensions._
import utopia.flow.time.Now
import utopia.flow.time.TimeExtensions._
import vf.optidepy.model.factory.deployment.DeploymentConfigFactory

import java.nio.file.Path
import java.time.Instant

object DeploymentConfigData extends FromModelFactoryWithSchema[DeploymentConfigData]
{
	// ATTRIBUTES	--------------------
	
	override lazy val schema = 
		ModelDeclaration(Vector(PropertyDeclaration("projectId", IntType, Single("project_id")), 
			PropertyDeclaration("outputDirectory", StringType, Single("output_directory")), 
			PropertyDeclaration("relativeInputDirectory", StringType, Single("relative_input_directory")), 
			PropertyDeclaration("name", StringType, isOptional = true), PropertyDeclaration("moduleId", 
			IntType, Single("module_id"), isOptional = true), PropertyDeclaration("created", InstantType, 
			isOptional = true), PropertyDeclaration("deprecatedAfter", InstantType, 
			Single("deprecated_after"), isOptional = true), PropertyDeclaration("usesBuildDirectories", 
			BooleanType, Single("uses_build_directories"), true), PropertyDeclaration("fileDeletionEnabled", 
			BooleanType, Single("file_deletion_enabled"), true)))
	
	
	// IMPLEMENTED	--------------------
	
	override protected def fromValidatedModel(valid: Model) = 
		DeploymentConfigData(valid("projectId").getInt, valid("outputDirectory").getString: Path, 
			valid("relativeInputDirectory").getString: Path, valid("name").getString, valid("moduleId").int, 
			valid("created").getInstant, valid("deprecatedAfter").instant, 
			valid("usesBuildDirectories").getBoolean, valid("fileDeletionEnabled").getBoolean)
}

/**
  * Represents settings used for deploying a project
  * @param projectId Id of the project which this configuration describes
  * @param outputDirectory Directory to which all the deployed files / sub-directories will be placed
  * @param relativeInputDirectory Path relative to this project's root directory which contains
  *  all deployed files. 
  * Empty if same as the project root path.
  * @param name Name of this deployment configuration. May be empty.
  * @param moduleId Id of the module this deployment is linked to. None if not linked to a
  *  specific versioned module.
  * @param created Time when this configuration was added
  * @param deprecatedAfter Time when this configuration was replaced with another
  * @param usesBuildDirectories Whether this project places deployed files in separate build directories
  * @param fileDeletionEnabled Whether output files not present in the input directories should be
  *  automatically removed
  * @author Mikko Hilpinen
  * @since 12.08.2024, v1.2
  */
case class DeploymentConfigData(projectId: Int, outputDirectory: Path, relativeInputDirectory: Path,
                                name: String = "", moduleId: Option[Int] = None, created: Instant = Now,
                                deprecatedAfter: Option[Instant] = None, usesBuildDirectories: Boolean = true,
                                fileDeletionEnabled: Boolean = true)
	extends DeploymentConfigFactory[DeploymentConfigData] with ModelConvertible
{
	// COMPUTED	--------------------
	
	/**
	  * Whether this deployment config has already been deprecated
	  */
	def isDeprecated = deprecatedAfter.isDefined
	/**
	  * Whether this deployment config is still valid (not deprecated)
	  */
	def isValid = !isDeprecated
	
	
	// IMPLEMENTED	--------------------
	
	override def toModel = Model(Vector(
		"projectId" -> projectId, "outputDirectory" -> outputDirectory.toJson,
		"relativeInputDirectory" -> relativeInputDirectory.toJson, "name" -> name, "moduleId" -> moduleId,
		"created" -> created, "deprecatedAfter" -> deprecatedAfter,
		"usesBuildDirectories" -> usesBuildDirectories, "fileDeletionEnabled" -> fileDeletionEnabled))
	
	override def withCreated(created: Instant) = copy(created = created)
	override def withDeprecatedAfter(deprecatedAfter: Instant) = copy(deprecatedAfter = Some(deprecatedAfter))
	override def withFileDeletionEnabled(fileDeletionEnabled: Boolean) = 
		copy(fileDeletionEnabled = fileDeletionEnabled)
	override def withModuleId(moduleId: Int) = copy(moduleId = Some(moduleId))
	override def withName(name: String) = copy(name = name)
	override def withOutputDirectory(outputDirectory: Path) = copy(outputDirectory = outputDirectory)
	override def withProjectId(projectId: Int) = copy(projectId = projectId)
	override def withRelativeInputDirectory(relativeInputDirectory: Path) = 
		copy(relativeInputDirectory = relativeInputDirectory)
	override def withUsesBuildDirectories(usesBuildDirectories: Boolean) = 
		copy(usesBuildDirectories = usesBuildDirectories)
		
	
	// OTHER    --------------------------
	
	/**
	 * @param branchName Name of the targeted branch
	 * @return A directory for that branch
	 */
	def outputDirectoryFor(branchName: String) = outputDirectory/branchName
	
	/**
	 * @param branch Name of the deployed branch
	 * @param deployment Targeted deployment
	 * @return A directory where that deployment should be stored
	 */
	def directoryForDeployment(branch: String, deployment: DeploymentData) =
		outputDirectory/s"$branch-build-${ deployment.index }-${deployment.created.toLocalDate.toString}"
}

