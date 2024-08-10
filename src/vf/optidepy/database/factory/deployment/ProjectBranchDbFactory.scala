package vf.optidepy.database.factory.deployment

import utopia.vault.nosql.factory.row.linked.CombiningFactory
import utopia.vault.nosql.template.Deprecatable
import vf.optidepy.database.factory.project.ProjectDbFactory
import vf.optidepy.model.combined.deployment.ProjectBranch
import vf.optidepy.model.stored.deployment.Branch
import vf.optidepy.model.stored.project.Project

/**
  * Used for reading project branches from the database
  * @author Mikko Hilpinen
  * @since 10.08.2024, v1.2
  */
object ProjectBranchDbFactory extends CombiningFactory[ProjectBranch, Branch, Project] with Deprecatable
{
	// IMPLEMENTED	--------------------
	
	override def childFactory = ProjectDbFactory
	
	override def nonDeprecatedCondition = 
		parentFactory.nonDeprecatedCondition && childFactory.nonDeprecatedCondition
	
	override def parentFactory = BranchDbFactory
	
	/**
	  * @param branch branch to wrap
	  * @param project project to attach to this branch
	  */
	override def apply(branch: Branch, project: Project) = ProjectBranch(branch, project)
}

