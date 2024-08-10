package vf.optidepy.model.combined.deployment

import utopia.flow.view.template.Extender
import utopia.vault.model.template.HasId
import vf.optidepy.model.factory.deployment.BranchFactoryWrapper
import vf.optidepy.model.partial.deployment.BranchData
import vf.optidepy.model.stored.deployment.Branch

/**
  * Common trait for combinations that add additional data to branches
  * @tparam Repr Type of the implementing class
  * @author Mikko Hilpinen
  * @since 10.08.2024, v1.2
  */
trait CombinedBranch[+Repr] 
	extends Extender[BranchData] with HasId[Int] with BranchFactoryWrapper[Branch, Repr]
{
	// ABSTRACT	--------------------
	
	/**
	  * Wrapped branch
	  */
	def branch: Branch
	
	
	// IMPLEMENTED	--------------------
	
	/**
	  * Id of this branch in the database
	  */
	override def id = branch.id
	
	override def wrapped = branch.data
	
	override protected def wrappedFactory = branch
}

