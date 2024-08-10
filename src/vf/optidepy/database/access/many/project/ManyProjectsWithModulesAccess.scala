package vf.optidepy.database.access.many.project

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.parse.file.FileExtensions._
import utopia.vault.database.Connection
import utopia.vault.nosql.view.ViewFactory
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.project.ProjectWithModulesDbFactory
import vf.optidepy.database.storable.library.VersionedModuleDbModel
import vf.optidepy.model.combined.project.ProjectWithModules

import java.nio.file.Path
import java.time.Instant

object ManyProjectsWithModulesAccess extends ViewFactory[ManyProjectsWithModulesAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): ManyProjectsWithModulesAccess = 
		_ManyProjectsWithModulesAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _ManyProjectsWithModulesAccess(override val accessCondition: Option[Condition]) 
		extends ManyProjectsWithModulesAccess
}

/**
  * A common trait for access points that return multiple projects with modules at a time
  * @author Mikko Hilpinen
  * @since 10.08.2024
  */
trait ManyProjectsWithModulesAccess 
	extends ManyProjectsAccessLike[ProjectWithModules, ManyProjectsWithModulesAccess]
{
	// COMPUTED	--------------------
	
	/**
	 * @return Copy of this access point which only includes projects that have 1 or more modules
	 */
	def havingModules = filter(moduleModel.index.isNotNull)
	
	/**
	  * names of the accessible versioned modules
	  */
	def moduleNames(implicit connection: Connection) = pullColumn(moduleModel.name.column)
		.flatMap { _.string }
	/**
	  * relative change list paths of the accessible versioned modules
	  */
	def moduleRelativeChangeListPaths(implicit connection: Connection) = 
		pullColumn(moduleModel.relativeChangeListPath.column).flatMap { _.string }.map { v => v: Path }
	/**
	  * relative artifact directories of the accessible versioned modules
	  */
	def moduleRelativeArtifactDirectories(implicit connection: Connection) = 
		pullColumn(moduleModel.relativeArtifactDirectory.column).flatMap { _.string }.map { v => v: Path }
	/**
	  * creation times of the accessible versioned modules
	  */
	def moduleCreationTimes(implicit connection: Connection) = 
		pullColumn(moduleModel.created.column).map { v => v.getInstant }
	/**
	  * deprecation times of the accessible versioned modules
	  */
	def moduleDeprecationTimes(implicit connection: Connection) = 
		pullColumn(moduleModel.deprecatedAfter.column).flatMap { v => v.instant }
	
	/**
	  * Model (factory) used for interacting the versioned modules associated with this project with modules
	  */
	protected def moduleModel = VersionedModuleDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = ProjectWithModulesDbFactory
	override protected def self = this
	
	override def apply(condition: Condition): ManyProjectsWithModulesAccess = 
		ManyProjectsWithModulesAccess(condition)
	
	
	// OTHER	--------------------
	
	/**
	  * Updates the deprecation times of the targeted versioned modules
	  * @param newDeprecatedAfter A new deprecated after to assign
	  * @return Whether any versioned module was affected
	  */
	def moduleDeprecationTimes_=(newDeprecatedAfter: Instant)(implicit connection: Connection) = 
		putColumn(moduleModel.deprecatedAfter.column, newDeprecatedAfter)
}

