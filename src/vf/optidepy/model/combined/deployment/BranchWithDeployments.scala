package vf.optidepy.model.combined.deployment

import vf.optidepy.model.stored.deployment.{Branch, Deployment}

object BranchWithDeployments
{
	// OTHER	--------------------
	
	/**
	  * @param branch branch to wrap
	  * @param deployments deployments to attach to this branch
	  * @return Combination of the specified branch and deployment
	  */
	def apply(branch: Branch, deployments: Seq[Deployment]): BranchWithDeployments = 
		_BranchWithDeployments(branch, deployments)
	
	
	// NESTED	--------------------
	
	/**
	  * @param branch branch to wrap
	  * @param deployments deployments to attach to this branch
	  */
	private case class _BranchWithDeployments(branch: Branch, deployments: Seq[Deployment]) 
		extends BranchWithDeployments
	{
		// IMPLEMENTED	--------------------
		
		override protected def wrap(factory: Branch) = copy(branch = factory)
	}
}

/**
  * Attaches 0-n Deployments to a Branch
  * @author Mikko Hilpinen
  * @since 10.08.2024, v1.2
  */
trait BranchWithDeployments extends CombinedBranch[BranchWithDeployments]
{
	// ABSTRACT	--------------------
	
	/**
	  * Deployments that are attached to this branch
	  */
	def deployments: Seq[Deployment]
}

