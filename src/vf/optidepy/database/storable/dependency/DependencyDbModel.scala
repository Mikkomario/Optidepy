package vf.optidepy.database.storable.dependency

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.model.immutable.Value
import utopia.flow.parse.file.FileExtensions._
import utopia.vault.model.immutable.{DbPropertyDeclaration, Storable}
import utopia.vault.model.template.{FromIdFactory, HasId, HasIdProperty}
import utopia.vault.nosql.storable.StorableFactory
import utopia.vault.nosql.storable.deprecation.DeprecatableAfter
import vf.optidepy.database.OptidepyTables
import vf.optidepy.model.factory.dependency.DependencyFactory
import vf.optidepy.model.partial.dependency.DependencyData
import vf.optidepy.model.stored.dependency.Dependency

import java.nio.file.Path
import java.time.Instant

/**
  * Used for constructing DependencyDbModel instances and for inserting dependencies to the database
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DependencyDbModel 
	extends StorableFactory[DependencyDbModel, Dependency, DependencyData] 
		with FromIdFactory[Int, DependencyDbModel] with HasIdProperty 
		with DependencyFactory[DependencyDbModel] with DeprecatableAfter[DependencyDbModel]
{
	// ATTRIBUTES	--------------------
	
	override lazy val id = DbPropertyDeclaration("id", index)
	
	/**
	  * Database property used for interacting with dependent project ids
	  */
	lazy val dependentProjectId = property("dependentProjectId")
	
	/**
	  * Database property used for interacting with used module ids
	  */
	lazy val usedModuleId = property("usedModuleId")
	
	/**
	  * Database property used for interacting with relative lib directories
	  */
	lazy val relativeLibDirectory = property("relativeLibDirectory")
	
	/**
	  * Database property used for interacting with library file names
	  */
	lazy val libraryFileName = property("libraryFileName")
	
	/**
	  * Database property used for interacting with creation times
	  */
	lazy val created = property("created")
	
	/**
	  * Database property used for interacting with deprecation times
	  */
	lazy val deprecatedAfter = property("deprecatedAfter")
	
	/**
	  * Database property used for interacting with library file paths
	  */
	lazy val libraryFilePath = property("libraryFilePath")
	
	
	// IMPLEMENTED	--------------------
	
	override def table = OptidepyTables.dependency
	
	override def apply(data: DependencyData) =
		apply(None, Some(data.dependentProjectId), Some(data.usedModuleId), data.relativeLibDirectory.toJson,
			data.libraryFileName, Some(data.created), data.deprecatedAfter)
	
	/**
	  * @param created Time when this dependency was registered
	  * @return A model containing only the specified created
	  */
	override def withCreated(created: Instant) = apply(created = Some(created))
	
	/**
	  * @param dependentProjectId Id of the project which this dependency concerns
	  * @return A model containing only the specified dependent project id
	  */
	override def withDependentProjectId(dependentProjectId: Int) = 
		apply(dependentProjectId = Some(dependentProjectId))
	
	/**
	  * @param deprecatedAfter Time when this dependency was replaced or removed. None while active.
	  * @return A model containing only the specified deprecated after
	  */
	override
		 def withDeprecatedAfter(deprecatedAfter: Instant) = apply(deprecatedAfter = Some(deprecatedAfter))
	
	override def withId(id: Int) = apply(id = Some(id))
	
	/**
	  * @param libraryFileName Name of the library file matching this dependency. 
	  * Empty if not applicable.
	  * @return A model containing only the specified library file name
	  */
	override def withLibraryFileName(libraryFileName: String) = apply(libraryFileName = libraryFileName)
	
	/**
	  * @param relativeLibDirectory Path to the directory where library jars will be placed. 
	  * Relative to the project's root path.
	  * @return A model containing only the specified relative lib directory
	  */
	override def withRelativeLibDirectory(relativeLibDirectory: Path) = 
		apply(relativeLibDirectory = relativeLibDirectory.toJson)
	
	/**
	  * @param usedModuleId Id of the module the referenced project uses
	  * @return A model containing only the specified used module id
	  */
	override def withUsedModuleId(usedModuleId: Int) = apply(usedModuleId = Some(usedModuleId))
	
	override protected def complete(id: Value, data: DependencyData) = Dependency(id.getInt, data)
}

/**
  * Used for interacting with Dependencies in the database
  * @param id dependency database id
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DependencyDbModel(id: Option[Int] = None, dependentProjectId: Option[Int] = None,
                             usedModuleId: Option[Int] = None, relativeLibDirectory: String = "",
                             libraryFileName: String = "",
	created: Option[Instant] = None, deprecatedAfter: Option[Instant] = None) 
	extends Storable with HasId[Option[Int]] with FromIdFactory[Int, DependencyDbModel] 
		with DependencyFactory[DependencyDbModel]
{
	// IMPLEMENTED	--------------------
	
	override def table = DependencyDbModel.table
	
	override def valueProperties = 
		Vector(DependencyDbModel.id.name -> id, 
			DependencyDbModel.dependentProjectId.name -> dependentProjectId, 
			DependencyDbModel.usedModuleId.name -> usedModuleId, 
			DependencyDbModel.relativeLibDirectory.name -> relativeLibDirectory, 
			DependencyDbModel.libraryFileName.name -> libraryFileName, 
			DependencyDbModel.created.name -> created, 
			DependencyDbModel.deprecatedAfter.name -> deprecatedAfter)
	
	/**
	  * @param created Time when this dependency was registered
	  * @return A new copy of this model with the specified created
	  */
	override def withCreated(created: Instant) = copy(created = Some(created))
	
	/**
	  * @param dependentProjectId Id of the project which this dependency concerns
	  * @return A new copy of this model with the specified dependent project id
	  */
	override def withDependentProjectId(dependentProjectId: Int) = 
		copy(dependentProjectId = Some(dependentProjectId))
	
	/**
	  * @param deprecatedAfter Time when this dependency was replaced or removed. None while active.
	  * @return A new copy of this model with the specified deprecated after
	  */
	override def withDeprecatedAfter(deprecatedAfter: Instant) = copy(deprecatedAfter = Some(deprecatedAfter))
	
	override def withId(id: Int) = copy(id = Some(id))
	
	/**
	  * @param libraryFileName Name of the library file matching this dependency. 
	  * Empty if not applicable.
	  * @return A new copy of this model with the specified library file name
	  */
	override def withLibraryFileName(libraryFileName: String) =
		copy(libraryFileName = libraryFileName)
	
	/**
	  * @param relativeLibDirectory Path to the directory where library jars will be placed. 
	  * Relative to the project's root path.
	  * @return A new copy of this model with the specified relative lib directory
	  */
	override def withRelativeLibDirectory(relativeLibDirectory: Path) = 
		copy(relativeLibDirectory = relativeLibDirectory.toJson)
	
	/**
	  * @param usedModuleId Id of the module the referenced project uses
	  * @return A new copy of this model with the specified used module id
	  */
	override def withUsedModuleId(usedModuleId: Int) = copy(usedModuleId = Some(usedModuleId))
}

