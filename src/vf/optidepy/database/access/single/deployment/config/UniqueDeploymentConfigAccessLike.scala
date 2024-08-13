package vf.optidepy.database.access.single.deployment.config

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.model.immutable.Value
import utopia.flow.parse.file.FileExtensions._
import utopia.vault.database.Connection
import utopia.vault.nosql.access.single.model.SingleModelAccess
import utopia.vault.nosql.access.template.model.DistinctModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.FilterableView
import vf.optidepy.database.storable.deployment.DeploymentConfigDbModel

import java.nio.file.Path
import java.time.Instant

/**
  * A common trait for access points which target individual deployment configs or similar items at a time
  * @tparam A Type of read (deployment configs -like) instances
  * @tparam Repr Type of this access point
  * @author Mikko Hilpinen
  * @since 12.08.2024, v1.2
  */
trait UniqueDeploymentConfigAccessLike[+A, +Repr] 
	extends SingleModelAccess[A] with DistinctModelAccess[A, Option[A], Value] with FilterableView[Repr] 
		with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * Id of the project which this configuration describes. 
	  * None if no deployment config (or value) was found.
	  */
	def projectId(implicit connection: Connection) = pullColumn(model.projectId.column).int
	
	/**
	  * Directory to which all the deployed files / sub-directories will be placed. 
	  * None if no deployment config (or value) was found.
	  */
	def outputDirectory(implicit connection: Connection) = 
		Some(pullColumn(model.outputDirectory.column).getString: Path)
	
	/**
	  * Path relative to this project's root directory which contains all deployed files. 
	  * Empty if same as the project root path. 
	  * None if no deployment config (or value) was found.
	  */
	def relativeInputDirectory(implicit connection: Connection) = 
		Some(pullColumn(model.relativeInputDirectory.column).getString: Path)
	
	/**
	  * Name of this deployment configuration. May be empty. 
	  * None if no deployment config (or value) was found.
	  */
	def name(implicit connection: Connection) = pullColumn(model.name.column).getString
	
	/**
	  * Id of the module this deployment is linked to. None if not linked to a specific versioned module. 
	  * None if no deployment config (or value) was found.
	  */
	def moduleId(implicit connection: Connection) = pullColumn(model.moduleId.column).int
	
	/**
	  * Time when this configuration was added. 
	  * None if no deployment config (or value) was found.
	  */
	def created(implicit connection: Connection) = pullColumn(model.created.column).instant
	
	/**
	  * Time when this configuration was replaced with another. 
	  * None if no deployment config (or value) was found.
	  */
	def deprecatedAfter(implicit connection: Connection) = pullColumn(model.deprecatedAfter.column).instant
	
	/**
	  * Whether this project places deployed files in separate build directories. 
	  * None if no deployment config (or value) was found.
	  */
	def usesBuildDirectories(implicit connection: Connection) = 
		pullColumn(model.usesBuildDirectories.column).boolean
	
	/**
	  * Whether output files not present in the input directories should be automatically removed. 
	  * None if no deployment config (or value) was found.
	  */
	def fileDeletionEnabled(implicit connection: Connection) = 
		pullColumn(model.fileDeletionEnabled.column).boolean
	
	/**
	  * Unique id of the accessible deployment config. None if no deployment config was accessible.
	  */
	def id(implicit connection: Connection) = pullColumn(index).int
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	protected def model = DeploymentConfigDbModel
	
	
	// OTHER	--------------------
	
	/**
	  * Updates the deprecation times of the targeted deployment configs
	  * @param newDeprecatedAfter A new deprecated after to assign
	  * @return Whether any deployment config was affected
	  */
	def deprecatedAfter_=(newDeprecatedAfter: Instant)(implicit connection: Connection) = 
		putColumn(model.deprecatedAfter.column, newDeprecatedAfter)
	
	/**
	  * Updates the module ids of the targeted deployment configs
	  * @param newModuleId A new module id to assign
	  * @return Whether any deployment config was affected
	  */
	def moduleId_=(newModuleId: Int)(implicit connection: Connection) = 
		putColumn(model.moduleId.column, newModuleId)
}

