package vf.optidepy.database.storable.library

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.model.immutable.Value
import utopia.flow.parse.file.FileExtensions._
import utopia.vault.model.immutable.{DbPropertyDeclaration, Storable}
import utopia.vault.model.template.{FromIdFactory, HasIdProperty}
import utopia.vault.nosql.storable.StorableFactory
import utopia.vault.nosql.storable.deprecation.DeprecatableAfter
import vf.optidepy.database.OptidepyTables
import vf.optidepy.model.factory.library.VersionedModuleFactory
import vf.optidepy.model.partial.library.VersionedModuleData
import vf.optidepy.model.stored.library.VersionedModule

import java.nio.file.Path
import java.time.Instant

/**
  * Used for constructing VersionedModuleDbModel instances and for inserting versioned modules to the database
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object VersionedModuleDbModel 
	extends StorableFactory[VersionedModuleDbModel, VersionedModule, VersionedModuleData] 
		with FromIdFactory[Int, VersionedModuleDbModel] with VersionedModuleFactory[VersionedModuleDbModel] 
		with DeprecatableAfter[VersionedModuleDbModel] with HasIdProperty
{
	// ATTRIBUTES	--------------------
	
	override lazy val id = DbPropertyDeclaration("id", index)
	
	/**
	  * Database property used for interacting with project ids
	  */
	lazy val projectId = property("projectId")
	
	/**
	  * Database property used for interacting with names
	  */
	lazy val name = property("name")
	
	/**
	  * Database property used for interacting with relative change list paths
	  */
	lazy val relativeChangeListPath = property("relativeChangeListPath")
	
	/**
	  * Database property used for interacting with relative artifact directories
	  */
	lazy val relativeArtifactDirectory = property("relativeArtifactDirectory")
	
	/**
	  * Database property used for interacting with creation times
	  */
	lazy val created = property("created")
	
	/**
	  * Database property used for interacting with deprecation times
	  */
	lazy val deprecatedAfter = property("deprecatedAfter")
	
	
	// IMPLEMENTED	--------------------
	
	override def table = OptidepyTables.versionedModule
	
	override def apply(data: VersionedModuleData) = 
		apply(None, Some(data.projectId), data.name, data.relativeChangeListPath.toJson, 
			data.relativeArtifactDirectory.toJson, Some(data.created), data.deprecatedAfter)
	
	/**
	  * @param created Time when this module was introduced
	  * @return A model containing only the specified created
	  */
	override def withCreated(created: Instant) = apply(created = Some(created))
	
	/**
	  * @param deprecatedAfter Time when this module was archived or removed
	  * @return A model containing only the specified deprecated after
	  */
	override
		 def withDeprecatedAfter(deprecatedAfter: Instant) = apply(deprecatedAfter = Some(deprecatedAfter))
	
	override def withId(id: Int) = apply(id = Some(id))
	
	/**
	  * @param name Name of this module
	  * @return A model containing only the specified name
	  */
	override def withName(name: String) = apply(name = name)
	
	/**
	  * @param projectId Id of the project this module is part of
	  * @return A model containing only the specified project id
	  */
	override def withProjectId(projectId: Int) = apply(projectId = Some(projectId))
	
	/**
	  * @param relativeArtifactDirectory A path relative to the project root directory, 
	  * which points to the directory where artifact jar files will be exported
	  * @return A model containing only the specified relative artifact directory
	  */
	override def withRelativeArtifactDirectory(relativeArtifactDirectory: Path) = 
		apply(relativeArtifactDirectory = relativeArtifactDirectory.toJson)
	
	/**
	  * @param relativeChangeListPath A path relative to the project root directory, 
	  * which points to this module's Changes.md file
	  * @return A model containing only the specified relative change list path
	  */
	override def withRelativeChangeListPath(relativeChangeListPath: Path) = 
		apply(relativeChangeListPath = relativeChangeListPath.toJson)
	
	override protected def complete(id: Value, data: VersionedModuleData) = VersionedModule(id.getInt, data)
}

/**
  * Used for interacting with VersionedModules in the database
  * @param id versioned module database id
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class VersionedModuleDbModel(id: Option[Int] = None, projectId: Option[Int] = None, name: String = "", 
	relativeChangeListPath: String = "", relativeArtifactDirectory: String = "", 
	created: Option[Instant] = None, deprecatedAfter: Option[Instant] = None) 
	extends Storable with FromIdFactory[Int, VersionedModuleDbModel] 
		with VersionedModuleFactory[VersionedModuleDbModel]
{
	// IMPLEMENTED	--------------------
	
	override def table = VersionedModuleDbModel.table
	
	override def valueProperties = 
		Vector(VersionedModuleDbModel.id.name -> id, VersionedModuleDbModel.projectId.name -> projectId, 
			VersionedModuleDbModel.name.name -> name, 
			VersionedModuleDbModel.relativeChangeListPath.name -> relativeChangeListPath, 
			VersionedModuleDbModel.relativeArtifactDirectory.name -> relativeArtifactDirectory, 
			VersionedModuleDbModel.created.name -> created, 
			VersionedModuleDbModel.deprecatedAfter.name -> deprecatedAfter)
	
	/**
	  * @param created Time when this module was introduced
	  * @return A new copy of this model with the specified created
	  */
	override def withCreated(created: Instant) = copy(created = Some(created))
	
	/**
	  * @param deprecatedAfter Time when this module was archived or removed
	  * @return A new copy of this model with the specified deprecated after
	  */
	override def withDeprecatedAfter(deprecatedAfter: Instant) = copy(deprecatedAfter = Some(deprecatedAfter))
	
	override def withId(id: Int) = copy(id = Some(id))
	
	/**
	  * @param name Name of this module
	  * @return A new copy of this model with the specified name
	  */
	override def withName(name: String) = copy(name = name)
	
	/**
	  * @param projectId Id of the project this module is part of
	  * @return A new copy of this model with the specified project id
	  */
	override def withProjectId(projectId: Int) = copy(projectId = Some(projectId))
	
	/**
	  * @param relativeArtifactDirectory A path relative to the project root directory, 
	  * which points to the directory where artifact jar files will be exported
	  * @return A new copy of this model with the specified relative artifact directory
	  */
	override def withRelativeArtifactDirectory(relativeArtifactDirectory: Path) = 
		copy(relativeArtifactDirectory = relativeArtifactDirectory.toJson)
	
	/**
	  * @param relativeChangeListPath A path relative to the project root directory, 
	  * which points to this module's Changes.md file
	  * @return A new copy of this model with the specified relative change list path
	  */
	override def withRelativeChangeListPath(relativeChangeListPath: Path) = 
		copy(relativeChangeListPath = relativeChangeListPath.toJson)
}

