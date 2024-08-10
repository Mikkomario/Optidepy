package vf.optidepy.database.access.single.deployment.branch

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.parse.file.FileExtensions._
import utopia.vault.database.Connection
import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.view.{NullDeprecatableView, ViewFactory}
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.deployment.ProjectBranchDbFactory
import vf.optidepy.database.storable.project.ProjectDbModel
import vf.optidepy.model.combined.deployment.ProjectBranch

import java.nio.file.Path
import java.time.Instant

object UniqueProjectBranchAccess extends ViewFactory[UniqueProjectBranchAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): UniqueProjectBranchAccess = 
		_UniqueProjectBranchAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _UniqueProjectBranchAccess(override val accessCondition: Option[Condition]) 
		extends UniqueProjectBranchAccess
}

/**
  * A common trait for access points that return distinct project branchs
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait UniqueProjectBranchAccess 
	extends UniqueBranchAccessLike[ProjectBranch, UniqueProjectBranchAccess] 
		with SingleRowModelAccess[ProjectBranch] with NullDeprecatableView[UniqueProjectBranchAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * Name of this project. 
	  * None if no project (or value) was found.
	  */
	def projectName(implicit connection: Connection) = pullColumn(projectModel.name.column).getString
	
	/**
	  * Path to the directory that contains this project. 
	  * None if no project (or value) was found.
	  */
	def projectRootPath(implicit connection: Connection) = 
		Some(pullColumn(projectModel.rootPath.column).getString: Path)
	
	/**
	  * Time when this project was introduced. 
	  * None if no project (or value) was found.
	  */
	def projectCreated(implicit connection: Connection) = pullColumn(projectModel.created.column).instant
	
	/**
	  * Time whe this project was removed / archived. 
	  * None if no project (or value) was found.
	  */
	def projectDeprecatedAfter(implicit connection: Connection) = 
		pullColumn(projectModel.deprecatedAfter.column).instant
	
	/**
	  * A database model (factory) used for interacting with the linked project
	  */
	protected def projectModel = ProjectDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = ProjectBranchDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): UniqueProjectBranchAccess = UniqueProjectBranchAccess(condition)
	
	
	// OTHER	--------------------
	
	/**
	  * Updates the deprecation times of the targeted projects
	  * @param newDeprecatedAfter A new deprecated after to assign
	  * @return Whether any project was affected
	  */
	def projectDeprecatedAfter_=(newDeprecatedAfter: Instant)(implicit connection: Connection) = 
		putColumn(projectModel.deprecatedAfter.column, newDeprecatedAfter)
}
