package vf.optidepy.database.factory.deployment

import utopia.vault.nosql.factory.row.linked.PossiblyCombiningFactory
import utopia.vault.nosql.template.Deprecatable
import vf.optidepy.database.factory.project.ProjectDbFactory
import vf.optidepy.model.combined.deployment.ProjectBranch
import vf.optidepy.model.stored.deployment.Branch
import vf.optidepy.model.stored.project.Project

/**
  * Used for reading project branchs from the database
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object ProjectBranchDbFactory 
	extends PossiblyCombiningFactory[ProjectBranch, Branch, Project] with Deprecatable
{
	// IMPLEMENTED	--------------------
	
	override def childFactory = ProjectDbFactory
	
	override def nonDeprecatedCondition = 
		parentFactory.nonDeprecatedCondition && childFactory.nonDeprecatedCondition
	
	override def parentFactory = BranchDbFactory
	
	override def apply(branch: Branch, project: Option[Project]) = ProjectBranch(branch, project)
}

