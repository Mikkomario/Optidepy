package vf.optidepy.database.access.single.project

import utopia.vault.nosql.access.single.model.SingleModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.NonDeprecatedView
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.project.ProjectWithModulesDbFactory
import vf.optidepy.database.storable.library.VersionedModuleDbModel
import vf.optidepy.database.storable.project.ProjectDbModel
import vf.optidepy.model.combined.project.ProjectWithModules

/**
  * Used for accessing individual projects with modules
  * @author Mikko Hilpinen
  * @since 10.08.2024, v1.2
  */
object DbProjectWithModules 
	extends SingleModelAccess[ProjectWithModules] with NonDeprecatedView[ProjectWithModules] with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * A database model (factory) used for interacting with the linked module
	  */
	protected def moduleModel = VersionedModuleDbModel
	
	/**
	  * A database model (factory) used for interacting with linked projects
	  */
	private def model = ProjectDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = ProjectWithModulesDbFactory
	
	
	// OTHER	--------------------
	
	/**
	  * @param id Database id of the targeted project with modules
	  * @return An access point to that project with modules
	  */
	def apply(id: Int) = DbSingleProjectWithModules(id)
	
	/**
	  * 
		@param additionalCondition Filter condition to apply in addition to this root view's condition. Should yield unique projects 
	  * with modules.
	  * @return An access point to the project with modules that satisfies the specified condition
	  */
	protected def filterDistinct(additionalCondition: Condition) = 
		UniqueProjectWithModulesAccess(mergeCondition(additionalCondition))
}

