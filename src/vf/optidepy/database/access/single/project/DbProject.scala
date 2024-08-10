package vf.optidepy.database.access.single.project

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.NonDeprecatedView
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.project.ProjectDbFactory
import vf.optidepy.database.storable.project.ProjectDbModel
import vf.optidepy.model.stored.project.Project

/**
  * Used for accessing individual projects
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbProject extends SingleRowModelAccess[Project] with NonDeprecatedView[Project] with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	protected def model = ProjectDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = ProjectDbFactory
	
	
	// OTHER	--------------------
	
	/**
	  * @param id Database id of the targeted project
	  * @return An access point to that project
	  */
	def apply(id: Int) = DbSingleProject(id)
	
	/**
	  * 
		@param additionalCondition Filter condition to apply in addition to this root view's condition. Should yield
	  *  unique projects.
	  * @return An access point to the project that satisfies the specified condition
	  */
	protected def filterDistinct(additionalCondition: Condition) = 
		UniqueProjectAccess(mergeCondition(additionalCondition))
}

