package vf.optidepy.model.combined.deployment

import utopia.flow.view.template.Extender
import utopia.vault.model.template.HasId
import vf.optidepy.model.factory.deployment.BranchFactoryWrapper
import vf.optidepy.model.partial.deployment.BranchData
import vf.optidepy.model.stored.deployment.{Branch, Deployment}

/**
  * Includes information about a specific branch deployment
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DeployedBranch(branch: Branch, deployment: Option[Deployment]) 
	extends Extender[BranchData] with HasId[Int] with BranchFactoryWrapper[Branch, DeployedBranch]
{
	// COMPUTED	--------------------
	
	/**
	  * Id of this branch in the database
	  */
	def id = branch.id
	
	
	// IMPLEMENTED	--------------------
	
	override def wrapped = branch.data
	
	override protected def wrappedFactory = branch
	
	override protected def wrap(factory: Branch) = copy(branch = factory)
}

