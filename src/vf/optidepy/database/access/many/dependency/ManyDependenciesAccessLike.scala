package vf.optidepy.database.access.many.dependency

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.parse.file.FileExtensions._
import utopia.vault.database.Connection
import utopia.vault.nosql.access.many.model.ManyModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.NullDeprecatableView
import vf.optidepy.database.storable.dependency.DependencyDbModel

import java.nio.file.Path
import java.time.Instant

/**
  * A common trait for access points which target multiple dependencies or similar instances at a time
  * @tparam A Type of read (dependencies -like) instances
  * @tparam Repr Type of this access point
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait ManyDependenciesAccessLike[+A, +Repr] 
	extends ManyModelAccess[A] with Indexed with NullDeprecatableView[Repr]
{
	// COMPUTED	--------------------
	
	/**
	  * dependent project ids of the accessible dependencies
	  */
	def dependentProjectIds(implicit connection: Connection) = 
		pullColumn(model.dependentProjectId.column).map { v => v.getInt }
	
	/**
	  * used module ids of the accessible dependencies
	  */
	def usedModuleIds(implicit connection: Connection) = 
		pullColumn(model.usedModuleId.column).map { v => v.getInt }
	
	/**
	  * relative lib directories of the accessible dependencies
	  */
	def relativeLibDirectories(implicit connection: Connection) = 
		pullColumn(model.relativeLibDirectory.column).flatMap { _.string }.map { v => v: Path }
	
	/**
	  * library file paths of the accessible dependencies
	  */
	def libraryFilePaths(implicit connection: Connection) = 
		pullColumn(model.libraryFilePath.column).flatMap { _.string }.map { v => Some(v: Path) }
	
	/**
	  * creation times of the accessible dependencies
	  */
	def creationTimes(implicit connection: Connection) = 
		pullColumn(model.created.column).map { v => v.getInstant }
	
	/**
	  * deprecation times of the accessible dependencies
	  */
	def deprecationTimes(implicit connection: Connection) = 
		pullColumn(model.deprecatedAfter.column).flatMap { v => v.instant }
	
	/**
	  * Unique ids of the accessible dependencies
	  */
	def ids(implicit connection: Connection) = pullColumn(index).map { v => v.getInt }
	
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
	def deprecationTimes_=(newDeprecatedAfter: Instant)(implicit connection: Connection) = 
		putColumn(model.deprecatedAfter.column, newDeprecatedAfter)
	
	/**
	  * @param dependentProjectId dependent project id to target
	  * @return Copy of this access point that only includes dependencies 
		with the specified dependent project id
	  */
	def inProject(dependentProjectId: Int) = filter(model.dependentProjectId.column <=> dependentProjectId)
	
	/**
	  * @param dependentProjectIds Targeted dependent project ids
	  * 
		@return Copy of this access point that only includes dependencies where dependent project id is within the
	  *  specified value set
	  */
	def inProjects(dependentProjectIds: Iterable[Int]) = 
		filter(model.dependentProjectId.column.in(dependentProjectIds))
	
	/**
	  * @param usedModuleId used module id to target
	  * @return Copy of this access point that only includes dependencies with the specified used module id
	  */
	def ofModule(usedModuleId: Int) = filter(model.usedModuleId.column <=> usedModuleId)
	
	/**
	  * @param usedModuleIds Targeted used module ids
	  * @return Copy of this access point that only includes dependencies where used module id is within the
	  *  specified value set
	  */
	def ofModules(usedModuleIds: Iterable[Int]) = filter(model.usedModuleId.column.in(usedModuleIds))
}

