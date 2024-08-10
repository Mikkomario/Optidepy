package vf.optidepy.database.access.many.library.module

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.parse.file.FileExtensions._
import utopia.vault.database.Connection
import utopia.vault.nosql.access.many.model.ManyModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.NullDeprecatableView
import vf.optidepy.database.storable.library.VersionedModuleDbModel

import java.nio.file.Path
import java.time.Instant

/**
  * A common trait for access points which target multiple versioned modules or similar instances at a time
  * @tparam A Type of read (versioned modules -like) instances
  * @tparam Repr Type of this access point
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait ManyVersionedModulesAccessLike[+A, +Repr] 
	extends ManyModelAccess[A] with Indexed with NullDeprecatableView[Repr]
{
	// COMPUTED	--------------------
	
	/**
	  * project ids of the accessible versioned modules
	  */
	def projectIds(implicit connection: Connection) = pullColumn(model.projectId.column).map { v => v.getInt }
	
	/**
	  * names of the accessible versioned modules
	  */
	def names(implicit connection: Connection) = pullColumn(model.name.column).flatMap { _.string }
	
	/**
	  * relative change list paths of the accessible versioned modules
	  */
	def relativeChangeListPaths(implicit connection: Connection) = 
		pullColumn(model.relativeChangeListPath.column).flatMap { _.string }.map { v => v: Path }
	
	/**
	  * relative artifact directories of the accessible versioned modules
	  */
	def relativeArtifactDirectories(implicit connection: Connection) = 
		pullColumn(model.relativeArtifactDirectory.column).flatMap { _.string }.map { v => v: Path }
	
	/**
	  * creation times of the accessible versioned modules
	  */
	def creationTimes(implicit connection: Connection) = 
		pullColumn(model.created.column).map { v => v.getInstant }
	
	/**
	  * deprecation times of the accessible versioned modules
	  */
	def deprecationTimes(implicit connection: Connection) = 
		pullColumn(model.deprecatedAfter.column).flatMap { v => v.instant }
	
	/**
	  * Unique ids of the accessible versioned modules
	  */
	def ids(implicit connection: Connection) = pullColumn(index).map { v => v.getInt }
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	protected def model = VersionedModuleDbModel
	
	
	// OTHER	--------------------
	
	/**
	  * Updates the deprecation times of the targeted versioned modules
	  * @param newDeprecatedAfter A new deprecated after to assign
	  * @return Whether any versioned module was affected
	  */
	def deprecationTimes_=(newDeprecatedAfter: Instant)(implicit connection: Connection) = 
		putColumn(model.deprecatedAfter.column, newDeprecatedAfter)
	
	/**
	  * @param projectId project id to target
	  * @return Copy of this access point that only includes versioned modules with the specified project id
	  */
	def inProject(projectId: Int) = filter(model.projectId.column <=> projectId)
	
	/**
	  * @param projectIds Targeted project ids
	  * @return Copy of this access point that only includes versioned modules where project id is within the
	  *  specified value set
	  */
	def inProjects(projectIds: Iterable[Int]) = filter(model.projectId.column.in(projectIds))
	
	/**
	  * @param name name to target
	  * @return Copy of this access point that only includes versioned modules with the specified name
	  */
	def withName(name: String) = filter(model.name.column <=> name)
	
	/**
	  * @param names Targeted names
	  * 
		@return Copy of this access point that only includes versioned modules where name is within the specified
	  *  value set
	  */
	def withNames(names: Iterable[String]) = filter(model.name.column.in(names))
}

