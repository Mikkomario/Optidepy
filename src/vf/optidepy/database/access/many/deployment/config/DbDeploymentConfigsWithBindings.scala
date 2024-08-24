package vf.optidepy.database.access.many.deployment.config

import utopia.vault.nosql.view.{NonDeprecatedView, UnconditionalView, ViewManyByIntIds}
import vf.optidepy.model.combined.deployment.DeploymentConfigWithBindings

/**
  * The root access point when targeting multiple detailed deployment configs at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbDeploymentConfigsWithBindings
	extends ManyDeploymentConfigsWithBindingsAccess with NonDeprecatedView[DeploymentConfigWithBindings]
		with ViewManyByIntIds[ManyDeploymentConfigsWithBindingsAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * A copy of this access point that includes historical (i.e. deprecated) detailed deployment configs
	  */
	def includingHistory = DbDeploymentConfigsWithBindingsIncludingHistory
	
	
	// NESTED	--------------------
	
	object DbDeploymentConfigsWithBindingsIncludingHistory
		extends ManyDeploymentConfigsWithBindingsAccess with UnconditionalView
}

