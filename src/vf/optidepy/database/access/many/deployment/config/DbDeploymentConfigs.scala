package vf.optidepy.database.access.many.deployment.config

import utopia.vault.nosql.view.{NonDeprecatedView, UnconditionalView, ViewManyByIntIds}
import vf.optidepy.model.stored.deployment.DeploymentConfig

/**
  * The root access point when targeting multiple deployment configs at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbDeploymentConfigs 
	extends ManyDeploymentConfigsAccess with NonDeprecatedView[DeploymentConfig] 
		with ViewManyByIntIds[ManyDeploymentConfigsAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * A copy of this access point that includes historical (i.e. deprecated) deployment configs
	  */
	def includingHistory = DbDeploymentConfigsIncludingHistory
	
	
	// NESTED	--------------------
	
	object DbDeploymentConfigsIncludingHistory extends ManyDeploymentConfigsAccess with UnconditionalView
}

