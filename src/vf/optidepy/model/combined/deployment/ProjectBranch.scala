package vf.optidepy.model.combined.deployment

import vf.optidepy.model.stored.deployment.Branch

object ProjectBranch
{
	// OTHER	--------------------
	
	/**
	  * @param branch branch to wrap
	  * @param config The deployment configuration where this branch is used
	  * @return Combination of the specified branch and project
	  */
	def apply(branch: Branch, config: ProjectDeploymentConfig): ProjectBranch = _ProjectBranch(branch, config)
	
	
	// NESTED	--------------------
	
	private case class _ProjectBranch(branch: Branch, config: ProjectDeploymentConfig) extends ProjectBranch
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
	  * The configuration where this branch is used
	  */
	def config: ProjectDeploymentConfig
	
	
	// COMPUTED --------------------
	
	/**
	 * @return Project where this branch is used
	 */
	def project = config.project
	
	
	// IMPLEMENTED  ----------------
	
	override def toString = s"$config/${ branch.name }"
}

