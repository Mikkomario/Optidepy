package vf.optidepy.database.factory.deployment

import utopia.vault.nosql.factory.multi.MultiCombiningFactory
import utopia.vault.nosql.template.Deprecatable
import vf.optidepy.model.combined.deployment.DetailedDeploymentConfig
import vf.optidepy.model.stored.deployment.{Binding, DeploymentConfig}

/**
  * Used for reading detailed deployment configs from the database
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DetailedDeploymentConfigDbFactory 
	extends MultiCombiningFactory[DetailedDeploymentConfig, DeploymentConfig, Binding] with Deprecatable
{
	// IMPLEMENTED	--------------------
	
	override def childFactory = BindingDbFactory
	
	override def isAlwaysLinked = false
	
	override def nonDeprecatedCondition = parentFactory.nonDeprecatedCondition
	
	override def parentFactory = DeploymentConfigDbFactory
	
	override def apply(config: DeploymentConfig, binding: Seq[Binding]) = 
		DetailedDeploymentConfig(config, binding)
}

