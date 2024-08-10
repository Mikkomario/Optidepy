package vf.optidepy.database.access.many.library.module

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.parse.file.FileExtensions._
import utopia.vault.database.Connection
import utopia.vault.nosql.access.many.model.ManyRowModelAccess
import utopia.vault.nosql.view.ViewFactory
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.VersionedProjectModuleDbFactory
import vf.optidepy.database.storable.project.ProjectDbModel
import vf.optidepy.model.combined.library.VersionedProjectModule

import java.nio.file.Path
import java.time.Instant

object ManyVersionedProjectModulesAccess extends ViewFactory[ManyVersionedProjectModulesAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): ManyVersionedProjectModulesAccess = 
		_ManyVersionedProjectModulesAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _ManyVersionedProjectModulesAccess(override val accessCondition: Option[Condition]) 
		extends ManyVersionedProjectModulesAccess
}

/**
  * A common trait for access points that return multiple versioned project modules at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024
  */
trait ManyVersionedProjectModulesAccess 
	extends ManyVersionedModulesAccessLike[VersionedProjectModule, ManyVersionedProjectModulesAccess] 
		with ManyRowModelAccess[VersionedProjectModule]
{
	// COMPUTED	--------------------
	
	/**
	  * names of the accessible projects
	  */
	def projectNames(implicit connection: Connection) = pullColumn(projectModel.name.column)
		.flatMap { _.string }
	
	/**
	  * root paths of the accessible projects
	  */
	def projectRootPaths(implicit connection: Connection) = 
		pullColumn(projectModel.rootPath.column).flatMap { _.string }.map { v => v: Path }
	
	/**
	  * creation times of the accessible projects
	  */
	def projectCreationTimes(implicit connection: Connection) = 
		pullColumn(projectModel.created.column).map { v => v.getInstant }
	
	/**
	  * deprecation times of the accessible projects
	  */
	def projectDeprecationTimes(implicit connection: Connection) = 
		pullColumn(projectModel.deprecatedAfter.column).flatMap { v => v.instant }
	
	/**
	  * Model (factory) used for interacting the projects associated with this versioned project module
	  */
	protected def projectModel = ProjectDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = VersionedProjectModuleDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): ManyVersionedProjectModulesAccess = 
		ManyVersionedProjectModulesAccess(condition)
	
	
	// OTHER	--------------------
	
	/**
	  * Updates the deprecation times of the targeted projects
	  * @param newDeprecatedAfter A new deprecated after to assign
	  * @return Whether any project was affected
	  */
	def projectDeprecationTimes_=(newDeprecatedAfter: Instant)(implicit connection: Connection) = 
		putColumn(projectModel.deprecatedAfter.column, newDeprecatedAfter)
}

