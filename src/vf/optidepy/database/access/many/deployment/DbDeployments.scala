package vf.optidepy.database.access.many.deployment

import utopia.vault.nosql.view.{UnconditionalView, ViewManyByIntIds}

/**
  * The root access point when targeting multiple deployments at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbDeployments 
	extends ManyDeploymentsAccess with UnconditionalView with ViewManyByIntIds[ManyDeploymentsAccess]

