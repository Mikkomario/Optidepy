package vf.optidepy.database.access.many.deployment.config

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.parse.file.FileExtensions._
import utopia.vault.database.Connection
import utopia.vault.nosql.access.many.model.ManyRowModelAccess
import utopia.vault.nosql.view.ViewFactory
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.deployment.ProjectDeploymentConfigDbFactory
import vf.optidepy.database.storable.project.ProjectDbModel
import vf.optidepy.model.combined.deployment.ProjectDeploymentConfig

import java.nio.file.Path
import java.time.Instant

object ManyProjectDeploymentConfigsAccess extends ViewFactory[ManyProjectDeploymentConfigsAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): ManyProjectDeploymentConfigsAccess = 
		_ManyProjectDeploymentConfigsAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _ManyProjectDeploymentConfigsAccess(override val accessCondition: Option[Condition]) 
		extends ManyProjectDeploymentConfigsAccess
}

/**
  * A common trait for access points that return multiple project deployment configs at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024
  */
trait ManyProjectDeploymentConfigsAccess 
	extends ManyDeploymentConfigsAccessLike[ProjectDeploymentConfig, ManyProjectDeploymentConfigsAccess] 
		with ManyRowModelAccess[ProjectDeploymentConfig]
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
	  * Model (factory) used for interacting the projects associated with this project deployment config
	  */
	protected def projectModel = ProjectDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = ProjectDeploymentConfigDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): ManyProjectDeploymentConfigsAccess = 
		ManyProjectDeploymentConfigsAccess(condition)
	
	
	// OTHER	--------------------
	
	/**
	  * Updates the deprecation times of the targeted projects
	  * @param newDeprecatedAfter A new deprecated after to assign
	  * @return Whether any project was affected
	  */
	def projectDeprecationTimes_=(newDeprecatedAfter: Instant)(implicit connection: Connection) = 
		putColumn(projectModel.deprecatedAfter.column, newDeprecatedAfter)
}

