package vf.optidepy.database.access.many.deployment.config

import utopia.vault.nosql.view.{NonDeprecatedView, UnconditionalView, ViewManyByIntIds}
import vf.optidepy.model.combined.deployment.DetailedDeploymentConfig

/**
  * The root access point when targeting multiple detailed deployment configs at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbDetailedDeploymentConfigs 
	extends ManyDetailedDeploymentConfigsAccess with NonDeprecatedView[DetailedDeploymentConfig] 
		with ViewManyByIntIds[ManyDetailedDeploymentConfigsAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * A copy of this access point that includes historical (i.e. deprecated) detailed deployment configs
	  */
	def includingHistory = DbDetailedDeploymentConfigsIncludingHistory
	
	
	// NESTED	--------------------
	
	object DbDetailedDeploymentConfigsIncludingHistory 
		extends ManyDetailedDeploymentConfigsAccess with UnconditionalView
}

