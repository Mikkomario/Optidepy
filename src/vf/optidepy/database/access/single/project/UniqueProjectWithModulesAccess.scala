package vf.optidepy.database.access.single.project

import utopia.vault.nosql.view.{NullDeprecatableView, ViewFactory}
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.project.ProjectWithModulesDbFactory
import vf.optidepy.database.storable.library.VersionedModuleDbModel
import vf.optidepy.model.combined.project.ProjectWithModules

object UniqueProjectWithModulesAccess extends ViewFactory[UniqueProjectWithModulesAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): UniqueProjectWithModulesAccess = 
		_UniqueProjectWithModulesAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _UniqueProjectWithModulesAccess(override val accessCondition: Option[Condition]) 
		extends UniqueProjectWithModulesAccess
}

/**
  * A common trait for access points that return distinct projects with modules
  * @author Mikko Hilpinen
  * @since 10.08.2024, v1.2
  */
trait UniqueProjectWithModulesAccess 
	extends UniqueProjectAccessLike[ProjectWithModules, UniqueProjectWithModulesAccess] 
		with NullDeprecatableView[UniqueProjectWithModulesAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * A database model (factory) used for interacting with the linked module
	  */
	protected def moduleModel = VersionedModuleDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = ProjectWithModulesDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): UniqueProjectWithModulesAccess = 
		UniqueProjectWithModulesAccess(condition)
}

