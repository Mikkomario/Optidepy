package vf.optidepy.database.factory.deployment

import utopia.vault.nosql.factory.row.linked.PossiblyCombiningFactory
import utopia.vault.nosql.template.Deprecatable
import vf.optidepy.model.combined.deployment.DeployedBranch
import vf.optidepy.model.stored.deployment.{Branch, Deployment}

/**
  * Used for reading deployed branchs from the database
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DeployedBranchDbFactory 
	extends PossiblyCombiningFactory[DeployedBranch, Branch, Deployment] with Deprecatable
{
	// IMPLEMENTED	--------------------
	
	override def childFactory = DeploymentDbFactory
	
	override def nonDeprecatedCondition = parentFactory.nonDeprecatedCondition
	
	override def parentFactory = BranchDbFactory
	
	override def apply(branch: Branch, deployment: Option[Deployment]) = DeployedBranch(branch, deployment)
}

