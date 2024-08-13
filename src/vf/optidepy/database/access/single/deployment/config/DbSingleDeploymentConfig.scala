package vf.optidepy.database.access.single.deployment.config

import utopia.vault.nosql.access.single.model.distinct.SingleIntIdModelAccess
import vf.optidepy.model.stored.deployment.DeploymentConfig

/**
  * An access point to individual deployment configs, based on their id
  * @author Mikko Hilpinen
  * @since 12.08.2024, v1.2
  */
case class DbSingleDeploymentConfig(id: Int) 
	extends UniqueDeploymentConfigAccess with SingleIntIdModelAccess[DeploymentConfig]

