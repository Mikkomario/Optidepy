package vf.optidepy.database.storable.project

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.model.immutable.Value
import utopia.flow.parse.file.FileExtensions._
import utopia.vault.model.immutable.{DbPropertyDeclaration, Storable}
import utopia.vault.model.template.{FromIdFactory, HasIdProperty}
import utopia.vault.nosql.storable.StorableFactory
import utopia.vault.nosql.storable.deprecation.DeprecatableAfter
import vf.optidepy.database.OptidepyTables
import vf.optidepy.model.factory.project.ProjectFactory
import vf.optidepy.model.partial.project.ProjectData
import vf.optidepy.model.stored.project.Project

import java.nio.file.Path
import java.time.Instant

/**
  * Used for constructing ProjectDbModel instances and for inserting projects to the database
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object ProjectDbModel 
	extends StorableFactory[ProjectDbModel, Project, ProjectData] with FromIdFactory[Int, ProjectDbModel] 
		with ProjectFactory[ProjectDbModel] with DeprecatableAfter[ProjectDbModel] with HasIdProperty
{
	// ATTRIBUTES	--------------------
	
	override lazy val id = DbPropertyDeclaration("id", index)
	
	/**
	  * Database property used for interacting with names
	  */
	lazy val name = property("name")
	
	/**
	  * Database property used for interacting with root paths
	  */
	lazy val rootPath = property("rootPath")
	
	/**
	  * Database property used for interacting with creation times
	  */
	lazy val created = property("created")
	
	/**
	  * Database property used for interacting with deprecation times
	  */
	lazy val deprecatedAfter = property("deprecatedAfter")
	
	
	// IMPLEMENTED	--------------------
	
	override def table = OptidepyTables.project
	
	override def apply(data: ProjectData) = 
		apply(None, data.name, data.rootPath.toJson, Some(data.created), data.deprecatedAfter)
	
	/**
	  * @param created Time when this project was introduced
	  * @return A model containing only the specified created
	  */
	override def withCreated(created: Instant) = apply(created = Some(created))
	
	/**
	  * @param deprecatedAfter Time whe this project was removed / archived
	  * @return A model containing only the specified deprecated after
	  */
	override
		 def withDeprecatedAfter(deprecatedAfter: Instant) = apply(deprecatedAfter = Some(deprecatedAfter))
	
	override def withId(id: Int) = apply(id = Some(id))
	
	/**
	  * @param name Name of this project
	  * @return A model containing only the specified name
	  */
	override def withName(name: String) = apply(name = name)
	
	/**
	  * @param rootPath Path to the directory that contains this project
	  * @return A model containing only the specified root path
	  */
	override def withRootPath(rootPath: Path) = apply(rootPath = rootPath.toJson)
	
	override protected def complete(id: Value, data: ProjectData) = Project(id.getInt, data)
}

/**
  * Used for interacting with Projects in the database
  * @param id project database id
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class ProjectDbModel(id: Option[Int] = None, name: String = "", rootPath: String = "", 
	created: Option[Instant] = None, deprecatedAfter: Option[Instant] = None) 
	extends Storable with FromIdFactory[Int, ProjectDbModel] with ProjectFactory[ProjectDbModel]
{
	// IMPLEMENTED	--------------------
	
	override def table = ProjectDbModel.table
	
	override def valueProperties = 
		Vector(ProjectDbModel.id.name -> id, ProjectDbModel.name.name -> name, 
			ProjectDbModel.rootPath.name -> rootPath, ProjectDbModel.created.name -> created, 
			ProjectDbModel.deprecatedAfter.name -> deprecatedAfter)
	
	/**
	  * @param created Time when this project was introduced
	  * @return A new copy of this model with the specified created
	  */
	override def withCreated(created: Instant) = copy(created = Some(created))
	
	/**
	  * @param deprecatedAfter Time whe this project was removed / archived
	  * @return A new copy of this model with the specified deprecated after
	  */
	override def withDeprecatedAfter(deprecatedAfter: Instant) = copy(deprecatedAfter = Some(deprecatedAfter))
	
	override def withId(id: Int) = copy(id = Some(id))
	
	/**
	  * @param name Name of this project
	  * @return A new copy of this model with the specified name
	  */
	override def withName(name: String) = copy(name = name)
	
	/**
	  * @param rootPath Path to the directory that contains this project
	  * @return A new copy of this model with the specified root path
	  */
	override def withRootPath(rootPath: Path) = copy(rootPath = rootPath.toJson)
}

