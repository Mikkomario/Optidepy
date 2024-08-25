package vf.optidepy.database.access.many.deployment

import utopia.vault.nosql.view.{NonDeprecatedView, UnconditionalView, ViewManyByIntIds}
import vf.optidepy.model.stored.deployment.Deployment

/**
  * The root access point when targeting multiple deployments at a time
  * @author Mikko Hilpinen
  * @since 24.08.2024, v1.2
  */
object DbDeployments 
	extends ManyDeploymentsAccess with NonDeprecatedView[Deployment] 
		with ViewManyByIntIds[ManyDeploymentsAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * A copy of this access point that includes historical (i.e. deprecated) deployments
	  */
	def includingHistory = DbDeploymentsIncludingHistory
	
	
	// NESTED	--------------------
	
	object DbDeploymentsIncludingHistory extends ManyDeploymentsAccess with UnconditionalView
}

