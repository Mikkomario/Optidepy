package vf.optidepy.model.combined.deployment

import vf.optidepy.model.stored.deployment.{Binding, DeploymentConfig}
import vf.optidepy.model.stored.project.Project

object ProjectDeploymentConfigWithBindings
{
	// OTHER    -----------------------------
	
	/**
	 * Combines project, deployment config and binding information into a single model
	 * @param config Deployment configuration to wrap
	 * @param project Project to include
	 * @param bindings Bindings to attach
	 * @return Combination of this information
	 */
	def apply(config: DeploymentConfig, project: Project, bindings: Seq[Binding]): ProjectDeploymentConfigWithBindings =
		_ProjectDeploymentConfigWithBindings(config, project, bindings)
	
	
	// NESTED   -----------------------------
	
	private case class _ProjectDeploymentConfigWithBindings(config: DeploymentConfig, project: Project,
	                                                        bindings: Seq[Binding])
		extends ProjectDeploymentConfigWithBindings
	{
		// IMPLEMENTED  ------------------------
		
		override protected def wrap(factory: DeploymentConfig) =
			copy(config = factory)
	}
}

/**
 * Contains deployment configuration information, standard project information,
 * as well as individual file / directory bindings.
 * @author Mikko Hilpinen
 * @since 10.08.2024, v1.2
 */
trait ProjectDeploymentConfigWithBindings
	extends ProjectDeploymentConfig with DeploymentConfigWithBindings
		with CombinedDeploymentConfig[ProjectDeploymentConfigWithBindings]
{
	// COMPUTED   ------------------------
	
	/**
	 * @return Directory bindings where input is absolute and output is relative
	 */
	def sourceCorrectedBindings = bindings.map { _.underSource(inputDirectory) }
}