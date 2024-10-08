package vf.optidepy.database.access.single.deployment.config

import utopia.vault.nosql.access.single.model.distinct.SingleIntIdModelAccess
import vf.optidepy.model.combined.deployment.DeploymentConfigWithBindings

/**
  * An access point to individual detailed deployment configs, based on their config id
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DbSingleDeploymentConfigWithBindings(id: Int)
	extends UniqueDeploymentConfigWithBindingsAccess with SingleIntIdModelAccess[DeploymentConfigWithBindings]

