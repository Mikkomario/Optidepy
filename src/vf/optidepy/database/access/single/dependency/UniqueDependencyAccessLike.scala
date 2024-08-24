package vf.optidepy.database.access.single.dependency

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.model.immutable.Value
import utopia.flow.parse.file.FileExtensions._
import utopia.vault.database.Connection
import utopia.vault.nosql.access.single.model.SingleModelAccess
import utopia.vault.nosql.access.template.model.DistinctModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.FilterableView
import vf.optidepy.database.storable.dependency.DependencyDbModel

import java.nio.file.Path
import java.time.Instant

/**
  * A common trait for access points which target individual dependencies or similar items at a time
  * @tparam A Type of read (dependencies -like) instances
  * @tparam Repr Type of this access point
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait UniqueDependencyAccessLike[+A, +Repr] 
	extends SingleModelAccess[A] with DistinctModelAccess[A, Option[A], Value] with FilterableView[Repr] 
		with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * Id of the project which this dependency concerns. 
	  * None if no dependency (or value) was found.
	  */
	def dependentProjectId(implicit connection: Connection) = pullColumn(model.dependentProjectId.column).int
	
	/**
	  * Id of the module the referenced project uses. 
	  * None if no dependency (or value) was found.
	  */
	def usedModuleId(implicit connection: Connection) = pullColumn(model.usedModuleId.column).int
	
	/**
	  * Path to the directory where library jars will be placed. 
	  * Relative to the project's root directrory.
	  * None if no dependency (or value) was found.
	  */
	def relativeLibDirectory(implicit connection: Connection) = 
		Some(pullColumn(model.relativeLibDirectory.column).getString: Path)
	
	/**
	  * Name of the library file matching this dependency. 
	  * Empty if not applicable. 
	  * None if no dependency (or value) was found.
	  */
	def libraryFileName(implicit connection: Connection) = pullColumn(model.libraryFileName.column).getString
	
	/**
	  * Time when this dependency was registered. 
	  * None if no dependency (or value) was found.
	  */
	def created(implicit connection: Connection) = pullColumn(model.created.column).instant
	
	/**
	  * Time when this dependency was replaced or removed. None while active. 
	  * None if no dependency (or value) was found.
	  */
	def deprecatedAfter(implicit connection: Connection) = pullColumn(model.deprecatedAfter.column).instant
	
	/**
	  * Unique id of the accessible dependency. None if no dependency was accessible.
	  */
	def id(implicit connection: Connection) = pullColumn(index).int
	
	/**
	  * Path to the library file matching this dependency. Relative to the project's root path. 
	  * None if not applicable. 
	  * None if no dependency (or value) was found.
	  */
	def libraryFilePath(implicit connection: Connection) = 
		Some(pullColumn(model.libraryFilePath.column).getString: Path)
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	protected def model = DependencyDbModel
	
	
	// OTHER	--------------------
	
	/**
	  * Updates the deprecation times of the targeted dependencies
	  * @param newDeprecatedAfter A new deprecated after to assign
	  * @return Whether any dependency was affected
	  */
	def deprecatedAfter_=(newDeprecatedAfter: Instant)(implicit connection: Connection) = 
		putColumn(model.deprecatedAfter.column, newDeprecatedAfter)
	
	/**
	  * Updates the library file names of the targeted dependencies
	  * @param newLibraryFileName A new library file name to assign
	  * @return Whether any dependency was affected
	  */
	def libraryFileName_=(newLibraryFileName: String)(implicit connection: Connection) = 
		putColumn(model.libraryFileName.column, newLibraryFileName)
}

