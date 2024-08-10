package vf.optidepy.model.combined.deployment

import vf.optidepy.model.stored.deployment.Branch
import vf.optidepy.model.stored.project.Project

object ProjectBranch
{
	// OTHER	--------------------
	
	/**
	  * @param branch branch to wrap
	  * @param project project to attach to this branch
	  * @return Combination of the specified branch and project
	  */
	def apply(branch: Branch, project: Project): ProjectBranch = _ProjectBranch(branch, project)
	
	
	// NESTED	--------------------
	
	/**
	  * @param branch branch to wrap
	  * @param project project to attach to this branch
	  */
	private case class _ProjectBranch(branch: Branch, project: Project) extends ProjectBranch
	{
		// IMPLEMENTED	--------------------
		
		override protected def wrap(factory: Branch) = copy(branch = factory)
	}
}

/**
  * Combines project and branch information
  * @author Mikko Hilpinen
  * @since 10.08.2024, v1.2
  */
trait ProjectBranch extends CombinedBranch[ProjectBranch]
{
	// ABSTRACT	--------------------
	
	/**
	  * The project that is attached to this branch
	  */
	def project: Project
}

