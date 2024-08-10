package vf.optidepy.database.factory.deployment

import utopia.vault.nosql.factory.multi.MultiCombiningFactory
import utopia.vault.nosql.template.Deprecatable
import vf.optidepy.model.combined.deployment.BranchWithDeployments
import vf.optidepy.model.stored.deployment.{Branch, Deployment}

/**
  * Used for reading branches with deployments from the database
  * @author Mikko Hilpinen
  * @since 10.08.2024, v1.2
  */
object BranchWithDeploymentsDbFactory 
	extends MultiCombiningFactory[BranchWithDeployments, Branch, Deployment] with Deprecatable
{
	// IMPLEMENTED	--------------------
	
	override def parentFactory = BranchDbFactory
	override def childFactory = DeploymentDbFactory
	
	override def isAlwaysLinked = false
	
	override def nonDeprecatedCondition = parentFactory.nonDeprecatedCondition
	
	/**
	  * @param branch branch to wrap
	  * @param deployments deployments to attach to this branch
	  */
	override def apply(branch: Branch, deployments: Seq[Deployment]) = BranchWithDeployments(branch, deployments)
}

