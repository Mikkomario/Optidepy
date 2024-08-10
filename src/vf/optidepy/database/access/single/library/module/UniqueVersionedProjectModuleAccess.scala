package vf.optidepy.database.access.single.library.module

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.parse.file.FileExtensions._
import utopia.vault.database.Connection
import utopia.vault.nosql.access.single.model.SingleChronoRowModelAccess
import utopia.vault.nosql.view.{NullDeprecatableView, ViewFactory}
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.VersionedProjectModuleDbFactory
import vf.optidepy.database.storable.project.ProjectDbModel
import vf.optidepy.model.combined.library.VersionedProjectModule

import java.nio.file.Path
import java.time.Instant

object UniqueVersionedProjectModuleAccess extends ViewFactory[UniqueVersionedProjectModuleAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): UniqueVersionedProjectModuleAccess = 
		_UniqueVersionedProjectModuleAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _UniqueVersionedProjectModuleAccess(override val accessCondition: Option[Condition]) 
		extends UniqueVersionedProjectModuleAccess
}

/**
  * A common trait for access points that return distinct versioned project modules
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait UniqueVersionedProjectModuleAccess 
	extends UniqueVersionedModuleAccessLike[VersionedProjectModule, UniqueVersionedProjectModuleAccess] 
		with SingleChronoRowModelAccess[VersionedProjectModule, UniqueVersionedProjectModuleAccess] 
		with NullDeprecatableView[UniqueVersionedProjectModuleAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * Name of this project. 
	  * None if no project (or value) was found.
	  */
	def projectName(implicit connection: Connection) = pullColumn(projectModel.name.column).getString
	
	/**
	  * Path to the directory that contains this project. 
	  * None if no project (or value) was found.
	  */
	def projectRootPath(implicit connection: Connection) = 
		Some(pullColumn(projectModel.rootPath.column).getString: Path)
	
	/**
	  * Time when this project was introduced. 
	  * None if no project (or value) was found.
	  */
	def projectCreated(implicit connection: Connection) = pullColumn(projectModel.created.column).instant
	
	/**
	  * Time whe this project was removed / archived. 
	  * None if no project (or value) was found.
	  */
	def projectDeprecatedAfter(implicit connection: Connection) = 
		pullColumn(projectModel.deprecatedAfter.column).instant
	
	/**
	  * A database model (factory) used for interacting with the linked project
	  */
	protected def projectModel = ProjectDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = VersionedProjectModuleDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): UniqueVersionedProjectModuleAccess = 
		UniqueVersionedProjectModuleAccess(condition)
	
	
	// OTHER	--------------------
	
	/**
	  * Updates the deprecation times of the targeted projects
	  * @param newDeprecatedAfter A new deprecated after to assign
	  * @return Whether any project was affected
	  */
	def projectDeprecatedAfter_=(newDeprecatedAfter: Instant)(implicit connection: Connection) = 
		putColumn(projectModel.deprecatedAfter.column, newDeprecatedAfter)
}

