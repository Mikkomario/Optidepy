package vf.optidepy.database.access.many.deployment.branch

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.parse.file.FileExtensions._
import utopia.vault.database.Connection
import utopia.vault.nosql.access.many.model.ManyRowModelAccess
import utopia.vault.nosql.view.ViewFactory
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.deployment.ProjectBranchDbFactory
import vf.optidepy.database.storable.project.ProjectDbModel
import vf.optidepy.model.combined.deployment.ProjectBranch

import java.nio.file.Path
import java.time.Instant

object ManyProjectBranchesAccess extends ViewFactory[ManyProjectBranchesAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): ManyProjectBranchesAccess = 
		_ManyProjectBranchesAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _ManyProjectBranchesAccess(override val accessCondition: Option[Condition]) 
		extends ManyProjectBranchesAccess
}

/**
  * A common trait for access points that return multiple project branches at a time
  * @author Mikko Hilpinen
  * @since 10.08.2024
  */
trait ManyProjectBranchesAccess 
	extends ManyBranchesAccessLike[ProjectBranch, ManyProjectBranchesAccess] 
		with ManyRowModelAccess[ProjectBranch]
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
	  * Model (factory) used for interacting the projects associated with this project branch
	  */
	protected def projectModel = ProjectDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = ProjectBranchDbFactory
	override protected def self = this
	
	override def apply(condition: Condition): ManyProjectBranchesAccess = ManyProjectBranchesAccess(condition)
	
	
	// OTHER	--------------------
	
	/**
	  * Updates the deprecation times of the targeted projects
	  * @param newDeprecatedAfter A new deprecated after to assign
	  * @return Whether any project was affected
	  */
	def projectDeprecationTimes_=(newDeprecatedAfter: Instant)(implicit connection: Connection) = 
		putColumn(projectModel.deprecatedAfter.column, newDeprecatedAfter)
}

