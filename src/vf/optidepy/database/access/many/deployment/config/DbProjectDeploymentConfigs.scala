package vf.optidepy.database.access.many.deployment.config

import utopia.vault.nosql.view.{NonDeprecatedView, UnconditionalView, ViewManyByIntIds}
import vf.optidepy.model.combined.deployment.ProjectDeploymentConfig

/**
  * The root access point when targeting multiple project deployment configs at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbProjectDeploymentConfigs 
	extends ManyProjectDeploymentConfigsAccess with NonDeprecatedView[ProjectDeploymentConfig] 
		with ViewManyByIntIds[ManyProjectDeploymentConfigsAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * A copy of this access point that includes historical (i.e. deprecated) project deployment configs
	  */
	def includingHistory = DbProjectDeploymentConfigsIncludingHistory
	
	
	// NESTED	--------------------
	
	object DbProjectDeploymentConfigsIncludingHistory 
		extends ManyProjectDeploymentConfigsAccess with UnconditionalView
}

