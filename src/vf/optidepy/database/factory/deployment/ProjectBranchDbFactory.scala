package vf.optidepy.database.factory.deployment

import utopia.vault.nosql.factory.row.linked.CombiningFactory
import utopia.vault.nosql.template.Deprecatable
import vf.optidepy.model.combined.deployment.{ProjectBranch, ProjectDeploymentConfig}
import vf.optidepy.model.stored.deployment.Branch

/**
  * Used for reading project branches from the database
  * @author Mikko Hilpinen
  * @since 10.08.2024, v1.2
  */
object ProjectBranchDbFactory extends CombiningFactory[ProjectBranch, Branch, ProjectDeploymentConfig] with Deprecatable
{
	// IMPLEMENTED	--------------------
	
	override def parentFactory = BranchDbFactory
	override def childFactory = ProjectDeploymentConfigDbFactory
	
	override def nonDeprecatedCondition = 
		parentFactory.nonDeprecatedCondition && childFactory.nonDeprecatedCondition
	
	override def apply(branch: Branch, config: ProjectDeploymentConfig) = ProjectBranch(branch, config)
}

