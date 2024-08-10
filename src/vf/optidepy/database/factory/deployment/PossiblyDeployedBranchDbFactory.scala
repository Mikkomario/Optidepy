package vf.optidepy.database.factory.deployment

import utopia.vault.nosql.factory.row.linked.PossiblyCombiningFactory
import utopia.vault.nosql.template.Deprecatable
import vf.optidepy.model.combined.deployment.PossiblyDeployedBranch
import vf.optidepy.model.stored.deployment.{Branch, Deployment}

/**
  * Used for reading possibly deployed branches from the database
  * @author Mikko Hilpinen
  * @since 10.08.2024, v1.2
  */
object PossiblyDeployedBranchDbFactory 
	extends PossiblyCombiningFactory[PossiblyDeployedBranch, Branch, Deployment] with Deprecatable
{
	// IMPLEMENTED	--------------------
	
	override def childFactory = DeploymentDbFactory
	
	override def nonDeprecatedCondition = parentFactory.nonDeprecatedCondition
	
	override def parentFactory = BranchDbFactory
	
	/**
	  * @param branch branch to wrap
	  * @param deployment deployment to attach to this branch
	  */
	override def apply(branch: Branch, deployment: Option[Deployment]) = 
		PossiblyDeployedBranch(branch, deployment)
}

