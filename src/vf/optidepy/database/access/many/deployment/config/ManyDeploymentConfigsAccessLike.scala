package vf.optidepy.database.access.many.deployment.config

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.parse.file.FileExtensions._
import utopia.vault.database.Connection
import utopia.vault.nosql.access.many.model.ManyModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.NullDeprecatableView
import vf.optidepy.database.storable.deployment.DeploymentConfigDbModel

import java.nio.file.Path
import java.time.Instant

/**
  * A common trait for access points which target multiple deployment configs or similar instances at a time
  * @tparam A Type of read (deployment configs -like) instances
  * @tparam Repr Type of this access point
  * @author Mikko Hilpinen
  * @since 12.08.2024, v1.2
  */
trait ManyDeploymentConfigsAccessLike[+A, +Repr] 
	extends ManyModelAccess[A] with Indexed with NullDeprecatableView[Repr]
{
	// COMPUTED	--------------------
	
	/**
	  * project ids of the accessible deployment configs
	  */
	def projectIds(implicit connection: Connection) = pullColumn(model.projectId.column).map { v => v.getInt }
	/**
	  * output directories of the accessible deployment configs
	  */
	def outputDirectories(implicit connection: Connection) = 
		pullColumn(model.outputDirectory.column).flatMap { _.string }.map { v => v: Path }
	/**
	  * relative input directories of the accessible deployment configs
	  */
	def relativeInputDirectories(implicit connection: Connection) = 
		pullColumn(model.relativeInputDirectory.column).flatMap { _.string }.map { v => v: Path }
	/**
	  * names of the accessible deployment configs
	  */
	def names(implicit connection: Connection) = pullColumn(model.name.column).flatMap { _.string }
	/**
	  * module ids of the accessible deployment configs
	  */
	def moduleIds(implicit connection: Connection) = pullColumn(model.moduleId.column).flatMap { v => v.int }
	/**
	  * creation times of the accessible deployment configs
	  */
	def creationTimes(implicit connection: Connection) = 
		pullColumn(model.created.column).map { v => v.getInstant }
	/**
	  * deprecation times of the accessible deployment configs
	  */
	def deprecationTimes(implicit connection: Connection) = 
		pullColumn(model.deprecatedAfter.column).flatMap { v => v.instant }
	/**
	  * use build directories of the accessible deployment configs
	  */
	def useBuildDirectories(implicit connection: Connection) = 
		pullColumn(model.usesBuildDirectories.column).map { v => v.getBoolean }
	/**
	  * file deletions are enabled of the accessible deployment configs
	  */
	def fileDeletionsAreEnabled(implicit connection: Connection) = 
		pullColumn(model.fileDeletionEnabled.column).map { v => v.getBoolean }
	/**
	  * Unique ids of the accessible deployment configs
	  */
	def ids(implicit connection: Connection) = pullColumn(index).map { v => v.getInt }
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	protected def model = DeploymentConfigDbModel
	
	
	// OTHER	--------------------
	
	/**
	 * @param projectId project id to target
	 * @return Copy of this access point that only includes deployment configs with the specified project id
	 */
	def ofProject(projectId: Int) = filter(model.projectId.column <=> projectId)
	/**
	 * @param projectIds Targeted project ids
	 * @return Copy of this access point that only includes deployment configs where project id is within the
	 *  specified value set
	 */
	def ofProjects(projectIds: Iterable[Int]) = filter(model.projectId.column.in(projectIds))
	
	/**
	 * @param name Targeted deployment configuration name
	 * @return Access to configurations with that name
	 */
	def withName(name: String) = filter(model.name <=> name)
	
	/**
	  * Updates the deprecation times of the targeted deployment configs
	  * @param newDeprecatedAfter A new deprecated after to assign
	  * @return Whether any deployment config was affected
	  */
	def deprecationTimes_=(newDeprecatedAfter: Instant)(implicit connection: Connection) = 
		putColumn(model.deprecatedAfter.column, newDeprecatedAfter)
	/**
	  * Updates the module ids of the targeted deployment configs
	  * @param newModuleId A new module id to assign
	  * @return Whether any deployment config was affected
	  */
	def moduleIds_=(newModuleId: Int)(implicit connection: Connection) = 
		putColumn(model.moduleId.column, newModuleId)
}

