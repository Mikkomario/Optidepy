package vf.optidepy.model.combined.deployment

import vf.optidepy.model.stored.deployment.{Branch, Deployment}

object PossiblyDeployedBranch
{
	// OTHER	--------------------
	
	/**
	  * @param branch branch to wrap
	  * @param deployment deployment to attach to this branch
	  * @return Combination of the specified branch and deployment
	  */
	def apply(branch: Branch, deployment: Option[Deployment]): PossiblyDeployedBranch = 
		_PossiblyDeployedBranch(branch, deployment)
	
	
	// NESTED	--------------------
	
	/**
	  * @param branch branch to wrap
	  * @param deployment deployment to attach to this branch
	  */
	private case class _PossiblyDeployedBranch(branch: Branch, deployment: Option[Deployment]) 
		extends PossiblyDeployedBranch
	{
		// IMPLEMENTED	--------------------
		
		override protected def wrap(factory: Branch) = copy(branch = factory)
	}
}

/**
  * Includes information about a specific branch deployment, if available.
  * @author Mikko Hilpinen
  * @since 10.08.2024, v1.2
  */
trait PossiblyDeployedBranch extends CombinedBranch[PossiblyDeployedBranch]
{
	// ABSTRACT	--------------------
	
	/**
	  * The deployment that is attached to this branch
	  */
	def deployment: Option[Deployment]
}

