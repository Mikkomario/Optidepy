package vf.optidepy.database.access.single.project

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.model.immutable.Value
import utopia.flow.parse.file.FileExtensions._
import utopia.vault.database.Connection
import utopia.vault.nosql.access.single.model.SingleModelAccess
import utopia.vault.nosql.access.template.model.DistinctModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.FilterableView
import vf.optidepy.database.storable.project.ProjectDbModel

import java.nio.file.Path
import java.time.Instant

/**
  * A common trait for access points which target individual projects or similar items at a time
  * @tparam A Type of read (projects -like) instances
  * @tparam Repr Type of this access point
  * @author Mikko Hilpinen
  * @since 12.08.2024, v1.2
  */
trait UniqueProjectAccessLike[+A, +Repr] 
	extends SingleModelAccess[A] with DistinctModelAccess[A, Option[A], Value] with FilterableView[Repr] 
		with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * Name of this project. 
	  * None if no project (or value) was found.
	  */
	def name(implicit connection: Connection) = pullColumn(model.name.column).getString
	
	/**
	  * Path to the directory that contains this project. 
	  * None if no project (or value) was found.
	  */
	def rootPath(implicit connection: Connection) = Some(pullColumn(model.rootPath.column).getString: Path)
	
	/**
	  * Path to the .idea directory, if applicable. Relative to this project's root directory. 
	  * None if no project (or value) was found.
	  */
	def relativeIdeaPath(implicit connection: Connection) = 
		Some(pullColumn(model.relativeIdeaPath.column).getString: Path)
	
	/**
	  * Time when this project was introduced. 
	  * None if no project (or value) was found.
	  */
	def created(implicit connection: Connection) = pullColumn(model.created.column).instant
	
	/**
	  * Time whe this project was removed / archived. 
	  * None if no project (or value) was found.
	  */
	def deprecatedAfter(implicit connection: Connection) = pullColumn(model.deprecatedAfter.column).instant
	
	/**
	  * Unique id of the accessible project. None if no project was accessible.
	  */
	def id(implicit connection: Connection) = pullColumn(index).int
	
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
	def deprecatedAfter_=(newDeprecatedAfter: Instant)(implicit connection: Connection) = 
		putColumn(model.deprecatedAfter.column, newDeprecatedAfter)
}

