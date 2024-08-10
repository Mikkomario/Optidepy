package vf.optidepy.database.access.single.dependency

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.NonDeprecatedView
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.dependency.ProjectDependencyDbFactory
import vf.optidepy.database.storable.dependency.DependencyDbModel
import vf.optidepy.database.storable.project.ProjectDbModel
import vf.optidepy.model.combined.dependency.ProjectDependency

/**
  * Used for accessing individual project dependencies
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbProjectDependency 
	extends SingleRowModelAccess[ProjectDependency] with NonDeprecatedView[ProjectDependency] with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * A database model (factory) used for interacting with linked dependencies
	  */
	protected def model = DependencyDbModel
	
	/**
	  * A database model (factory) used for interacting with the linked project
	  */
	protected def projectModel = ProjectDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = ProjectDependencyDbFactory
	
	
	// OTHER	--------------------
	
	/**
	  * @param id Database id of the targeted project dependency
	  * @return An access point to that project dependency
	  */
	def apply(id: Int) = DbSingleProjectDependency(id)
	
	/**
	  * 
		@param additionalCondition Filter condition to apply in addition to this root view's condition. Should yield
	  *  unique project dependencies.
	  * @return An access point to the project dependency that satisfies the specified condition
	  */
	protected def filterDistinct(additionalCondition: Condition) = 
		UniqueProjectDependencyAccess(mergeCondition(additionalCondition))
}

