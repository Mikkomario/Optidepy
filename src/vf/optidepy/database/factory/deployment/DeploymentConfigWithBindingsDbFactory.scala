package vf.optidepy.database.factory.deployment

import utopia.vault.nosql.factory.multi.MultiCombiningFactory
import utopia.vault.nosql.template.Deprecatable
import vf.optidepy.model.combined.deployment.DeploymentConfigWithBindings
import vf.optidepy.model.stored.deployment.{Binding, DeploymentConfig}

/**
  * Used for reading detailed deployment configs from the database
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DeploymentConfigWithBindingsDbFactory
	extends MultiCombiningFactory[DeploymentConfigWithBindings, DeploymentConfig, Binding] with Deprecatable
{
	// IMPLEMENTED	--------------------
	
	override def parentFactory = DeploymentConfigDbFactory
	override def childFactory = BindingDbFactory
	
	override def isAlwaysLinked = false
	
	override def nonDeprecatedCondition = parentFactory.nonDeprecatedCondition
	
	override def apply(config: DeploymentConfig, binding: Seq[Binding]) = 
		DeploymentConfigWithBindings(config, binding)
}

