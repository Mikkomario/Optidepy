package vf.optidepy.database.storable.deployment

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.model.immutable.Value
import utopia.flow.parse.file.FileExtensions._
import utopia.vault.model.immutable.{DbPropertyDeclaration, Storable}
import utopia.vault.model.template.{FromIdFactory, HasIdProperty}
import utopia.vault.nosql.storable.StorableFactory
import utopia.vault.nosql.storable.deprecation.DeprecatableAfter
import vf.optidepy.database.OptidepyTables
import vf.optidepy.model.factory.deployment.DeploymentConfigFactory
import vf.optidepy.model.partial.deployment.DeploymentConfigData
import vf.optidepy.model.stored.deployment.DeploymentConfig

import java.nio.file.Path
import java.time.Instant

/**
  * 
	Used for constructing DeploymentConfigDbModel instances and for inserting deployment configs to the database
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DeploymentConfigDbModel 
	extends StorableFactory[DeploymentConfigDbModel, DeploymentConfig, DeploymentConfigData] 
		with FromIdFactory[Int, DeploymentConfigDbModel] 
		with DeploymentConfigFactory[DeploymentConfigDbModel] with DeprecatableAfter[DeploymentConfigDbModel]
		with HasIdProperty
{
	// ATTRIBUTES	--------------------
	
	override lazy val id = DbPropertyDeclaration("id", index)
	
	/**
	  * Database property used for interacting with project ids
	  */
	lazy val projectId = property("projectId")
	
	/**
	  * Database property used for interacting with output directories
	  */
	lazy val outputDirectory = property("outputDirectory")
	
	/**
	  * Database property used for interacting with relative input directories
	  */
	lazy val relativeInputDirectory = property("relativeInputDirectory")
	
	/**
	  * Database property used for interacting with creation times
	  */
	lazy val created = property("created")
	
	/**
	  * Database property used for interacting with deprecation times
	  */
	lazy val deprecatedAfter = property("deprecatedAfter")
	
	/**
	  * Database property used for interacting with use build directories
	  */
	lazy val usesBuildDirectories = property("usesBuildDirectories")
	
	/**
	  * Database property used for interacting with file deletions are enabled
	  */
	lazy val fileDeletionEnabled = property("fileDeletionEnabled")
	
	
	// IMPLEMENTED	--------------------
	
	override def table = OptidepyTables.deploymentConfig
	
	override def apply(data: DeploymentConfigData) = {
		val inputDir = data.relativeInputDirectory match {
			case Some(dir) => dir.toJson
			case None => ""
		}
		apply(None, Some(data.projectId), data.outputDirectory.toJson,
			inputDir, Some(data.created), data.deprecatedAfter,
			Some(data.usesBuildDirectories), Some(data.fileDeletionEnabled))
	}
	
	/**
	  * @param created Time when this configuration was added
	  * @return A model containing only the specified created
	  */
	override def withCreated(created: Instant) = apply(created = Some(created))
	
	/**
	  * @param deprecatedAfter Time when this configuration was replaced with another
	  * @return A model containing only the specified deprecated after
	  */
	override
		 def withDeprecatedAfter(deprecatedAfter: Instant) = apply(deprecatedAfter = Some(deprecatedAfter))
	
	/**
	  * @param fileDeletionEnabled Whether output files not present in the input directories should be
	  *  automatically removed
	  * @return A model containing only the specified file deletion enabled
	  */
	override def withFileDeletionEnabled(fileDeletionEnabled: Boolean) = 
		apply(fileDeletionEnabled = Some(fileDeletionEnabled))
	
	override def withId(id: Int) = apply(id = Some(id))
	
	/**
	  * @param outputDirectory Directory to which all the deployed files / sub-directories will be placed
	  * @return A model containing only the specified output directory
	  */
	override def withOutputDirectory(outputDirectory: Path) = apply(outputDirectory = outputDirectory.toJson)
	
	/**
	  * @param projectId Id of the project which this configuration describes
	  * @return A model containing only the specified project id
	  */
	override def withProjectId(projectId: Int) = apply(projectId = Some(projectId))
	
	/**
	  * @param relativeInputDirectory Path relative to this project's root directory which contains
	  *  all deployed files. 
	  * None if the same as the project root path.
	  * @return A model containing only the specified relative input directory
	  */
	override def withRelativeInputDirectory(relativeInputDirectory: Path) = 
		apply(relativeInputDirectory = relativeInputDirectory.toJson)
	
	/**
	  * @param usesBuildDirectories Whether this project places deployed files in separate build directories
	  * @return A model containing only the specified uses build directories
	  */
	override def withUsesBuildDirectories(usesBuildDirectories: Boolean) = 
		apply(usesBuildDirectories = Some(usesBuildDirectories))
	
	override protected def complete(id: Value, data: DeploymentConfigData) = DeploymentConfig(id.getInt, data)
}

/**
  * Used for interacting with DeploymentConfigs in the database
  * @param id deployment config database id
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DeploymentConfigDbModel(id: Option[Int] = None, projectId: Option[Int] = None, 
	outputDirectory: String = "", relativeInputDirectory: String = "", created: Option[Instant] = None, 
	deprecatedAfter: Option[Instant] = None, usesBuildDirectories: Option[Boolean] = None, 
	fileDeletionEnabled: Option[Boolean] = None) 
	extends Storable with FromIdFactory[Int, DeploymentConfigDbModel] 
		with DeploymentConfigFactory[DeploymentConfigDbModel]
{
	// IMPLEMENTED	--------------------
	
	override def table = DeploymentConfigDbModel.table
	
	override def valueProperties = 
		Vector(DeploymentConfigDbModel.id.name -> id, DeploymentConfigDbModel.projectId.name -> projectId, 
			DeploymentConfigDbModel.outputDirectory.name -> outputDirectory, 
			DeploymentConfigDbModel.relativeInputDirectory.name -> relativeInputDirectory, 
			DeploymentConfigDbModel.created.name -> created, 
			DeploymentConfigDbModel.deprecatedAfter.name -> deprecatedAfter, 
			DeploymentConfigDbModel.usesBuildDirectories.name -> usesBuildDirectories, 
			DeploymentConfigDbModel.fileDeletionEnabled.name -> fileDeletionEnabled)
	
	/**
	  * @param created Time when this configuration was added
	  * @return A new copy of this model with the specified created
	  */
	override def withCreated(created: Instant) = copy(created = Some(created))
	
	/**
	  * @param deprecatedAfter Time when this configuration was replaced with another
	  * @return A new copy of this model with the specified deprecated after
	  */
	override def withDeprecatedAfter(deprecatedAfter: Instant) = copy(deprecatedAfter = Some(deprecatedAfter))
	
	/**
	  * @param fileDeletionEnabled Whether output files not present in the input directories should be
	  *  automatically removed
	  * @return A new copy of this model with the specified file deletion enabled
	  */
	override def withFileDeletionEnabled(fileDeletionEnabled: Boolean) = 
		copy(fileDeletionEnabled = Some(fileDeletionEnabled))
	
	override def withId(id: Int) = copy(id = Some(id))
	
	/**
	  * @param outputDirectory Directory to which all the deployed files / sub-directories will be placed
	  * @return A new copy of this model with the specified output directory
	  */
	override def withOutputDirectory(outputDirectory: Path) = copy(outputDirectory = outputDirectory.toJson)
	
	/**
	  * @param projectId Id of the project which this configuration describes
	  * @return A new copy of this model with the specified project id
	  */
	override def withProjectId(projectId: Int) = copy(projectId = Some(projectId))
	
	/**
	  * @param relativeInputDirectory Path relative to this project's root directory which contains
	  *  all deployed files. 
	  * None if the same as the project root path.
	  * @return A new copy of this model with the specified relative input directory
	  */
	override def withRelativeInputDirectory(relativeInputDirectory: Path) = 
		copy(relativeInputDirectory = relativeInputDirectory.toJson)
	
	/**
	  * @param usesBuildDirectories Whether this project places deployed files in separate build directories
	  * @return A new copy of this model with the specified uses build directories
	  */
	override def withUsesBuildDirectories(usesBuildDirectories: Boolean) = 
		copy(usesBuildDirectories = Some(usesBuildDirectories))
}

