package vf.optidepy.model.combined.deployment

import vf.optidepy.model.stored.deployment.{Binding, DeploymentConfig}

/**
 * Attaches binding, as well as branch information to a deployment configuration
 * @param config The wrapped configuration
 * @param bindings Bindings that specify how individual files or directories are deployed
 * @param branches Branches used in this configuration, each connected to their latest deployment, if applicable
 * @author Mikko Hilpinen
 * @since 23.08.2024, v1.2
 */
case class DetailedDeploymentConfig(config: DeploymentConfig, bindings: Seq[Binding],
                                    branches: Seq[PossiblyDeployedBranch])
	extends DeploymentConfigWithBindings with CombinedDeploymentConfig[DetailedDeploymentConfig]
{
	override protected def wrap(factory: DeploymentConfig): DetailedDeploymentConfig = copy(config = factory)
}