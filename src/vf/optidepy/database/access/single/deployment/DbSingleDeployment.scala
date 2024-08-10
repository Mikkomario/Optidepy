package vf.optidepy.database.access.single.deployment

import utopia.vault.nosql.access.single.model.distinct.SingleIntIdModelAccess
import vf.optidepy.model.stored.deployment.Deployment

/**
  * An access point to individual deployments, based on their id
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DbSingleDeployment(id: Int) extends UniqueDeploymentAccess with SingleIntIdModelAccess[Deployment]

