package vf.optidepy.database.access.single.library.module

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.model.immutable.Value
import utopia.flow.parse.file.FileExtensions._
import utopia.vault.database.Connection
import utopia.vault.nosql.access.single.model.SingleModelAccess
import utopia.vault.nosql.access.template.model.DistinctModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.FilterableView
import vf.optidepy.database.storable.library.VersionedModuleDbModel

import java.nio.file.Path
import java.time.Instant

/**
  * A common trait for access points which target individual versioned modules or similar items at a time
  * @tparam A Type of read (versioned modules -like) instances
  * @tparam Repr Type of this access point
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait UniqueVersionedModuleAccessLike[+A, +Repr] 
	extends SingleModelAccess[A] with DistinctModelAccess[A, Option[A], Value] with FilterableView[Repr] 
		with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * Id of the project this module is part of. 
	  * None if no versioned module (or value) was found.
	  */
	def projectId(implicit connection: Connection) = pullColumn(model.projectId.column).int
	
	/**
	  * Name of this module. 
	  * None if no versioned module (or value) was found.
	  */
	def name(implicit connection: Connection) = pullColumn(model.name.column).getString
	
	/**
	  * A path relative to the project root directory, which points to this module's Changes.md file. 
	  * None if no versioned module (or value) was found.
	  */
	def relativeChangeListPath(implicit connection: Connection) = 
		Some(pullColumn(model.relativeChangeListPath.column).getString: Path)
	
	/**
	  * A path relative to the project root directory, 
	  * which points to the directory where artifact jar files will be exported. 
	  * None if no versioned module (or value) was found.
	  */
	def relativeArtifactDirectory(implicit connection: Connection) = 
		Some(pullColumn(model.relativeArtifactDirectory.column).getString: Path)
	
	/**
	  * Time when this module was introduced. 
	  * None if no versioned module (or value) was found.
	  */
	def created(implicit connection: Connection) = pullColumn(model.created.column).instant
	
	/**
	  * Time when this module was archived or removed. 
	  * None if no versioned module (or value) was found.
	  */
	def deprecatedAfter(implicit connection: Connection) = pullColumn(model.deprecatedAfter.column).instant
	
	/**
	  * Unique id of the accessible versioned module. None if no versioned module was accessible.
	  */
	def id(implicit connection: Connection) = pullColumn(index).int
	
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
	def deprecatedAfter_=(newDeprecatedAfter: Instant)(implicit connection: Connection) = 
		putColumn(model.deprecatedAfter.column, newDeprecatedAfter)
}

