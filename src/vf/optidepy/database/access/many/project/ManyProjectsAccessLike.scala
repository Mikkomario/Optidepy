package vf.optidepy.database.access.many.project

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.parse.file.FileExtensions._
import utopia.vault.database.Connection
import utopia.vault.nosql.access.many.model.ManyModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.NullDeprecatableView
import vf.optidepy.database.storable.project.ProjectDbModel

import java.nio.file.Path
import java.time.Instant

/**
  * A common trait for access points which target multiple projects or similar instances at a time
  * @tparam A Type of read (projects -like) instances
  * @tparam Repr Type of this access point
  * @author Mikko Hilpinen
  * @since 12.08.2024, v1.2
  */
trait ManyProjectsAccessLike[+A, +Repr] 
	extends ManyModelAccess[A] with Indexed with NullDeprecatableView[Repr]
{
	// COMPUTED	--------------------
	
	/**
	  * names of the accessible projects
	  */
	def names(implicit connection: Connection) = pullColumn(model.name.column).flatMap { _.string }
	
	/**
	  * root paths of the accessible projects
	  */
	def rootPaths(implicit connection: Connection) = 
		pullColumn(model.rootPath.column).flatMap { _.string }.map { v => v: Path }
	
	/**
	  * relative idea paths of the accessible projects
	  */
	def relativeIdeaPaths(implicit connection: Connection) = 
		pullColumn(model.relativeIdeaPath.column).flatMap { _.string }.map { v => Some(v: Path) }
	
	/**
	  * creation times of the accessible projects
	  */
	def creationTimes(implicit connection: Connection) = 
		pullColumn(model.created.column).map { v => v.getInstant }
	
	/**
	  * deprecation times of the accessible projects
	  */
	def deprecationTimes(implicit connection: Connection) = 
		pullColumn(model.deprecatedAfter.column).flatMap { v => v.instant }
	
	/**
	  * Unique ids of the accessible projects
	  */
	def ids(implicit connection: Connection) = pullColumn(index).map { v => v.getInt }
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	protected def model = ProjectDbModel
	
	
	// OTHER	--------------------
	
	/**
	  * Updates the deprecation times of the targeted projects
	  * @param newDeprecatedAfter A new deprecated after to assign
	  * @return Whether any project was affected
	  */
	def deprecationTimes_=(newDeprecatedAfter: Instant)(implicit connection: Connection) = 
		putColumn(model.deprecatedAfter.column, newDeprecatedAfter)
	
	/**
	  * @param name name to target
	  * @return Copy of this access point that only includes projects with the specified name
	  */
	def withName(name: String) = filter(model.name.column <=> name)
	
	/**
	  * @param names Targeted names
	  * 
		@return Copy of this access point that only includes projects where name is within the specified value set
	  */
	def withNames(names: Iterable[String]) = filter(model.name.column.in(names))
}

