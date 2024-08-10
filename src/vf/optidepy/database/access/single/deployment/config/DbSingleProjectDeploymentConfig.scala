package vf.optidepy.database.access.single.deployment.config

import utopia.vault.nosql.access.single.model.distinct.SingleIntIdModelAccess
import vf.optidepy.model.combined.deployment.ProjectDeploymentConfig

/**
  * An access point to individual project deployment configs, based on their config id
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DbSingleProjectDeploymentConfig(id: Int) 
	extends UniqueProjectDeploymentConfigAccess with SingleIntIdModelAccess[ProjectDeploymentConfig]

