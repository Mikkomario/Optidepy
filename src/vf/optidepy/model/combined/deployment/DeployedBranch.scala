package vf.optidepy.model.combined.deployment

import vf.optidepy.model.stored.deployment.{Branch, Deployment}

object DeployedBranch
{
	// OTHER	--------------------
	
	/**
	  * @param branch branch to wrap
	  * @param deployment deployment to attach to this branch
	  * @return Combination of the specified branch and deployment
	  */
	def apply(branch: Branch, deployment: Deployment): DeployedBranch = _DeployedBranch(branch, deployment)
	
	
	// NESTED	--------------------
	
	/**
	  * @param branch branch to wrap
	  * @param deployment deployment to attach to this branch
	  */
	private case class _DeployedBranch(branch: Branch, deployment: Deployment) extends DeployedBranch
	{
		// IMPLEMENTED	--------------------
		
		override protected def wrap(factory: Branch) = copy(branch = factory)
	}
}

/**
  * Includes information about a specific branch deployment
  * @author Mikko Hilpinen
  * @since 10.08.2024, v1.2
  */
trait DeployedBranch extends CombinedBranch[DeployedBranch]
{
	// ABSTRACT	--------------------
	
	/**
	  * The deployment that is attached to this branch
	  */
	def deployment: Deployment
}

