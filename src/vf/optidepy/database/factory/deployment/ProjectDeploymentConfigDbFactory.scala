package vf.optidepy.database.factory.deployment

import utopia.vault.nosql.factory.row.linked.CombiningFactory
import utopia.vault.nosql.template.Deprecatable
import vf.optidepy.database.factory.project.ProjectDbFactory
import vf.optidepy.model.combined.deployment.ProjectDeploymentConfig
import vf.optidepy.model.stored.deployment.DeploymentConfig
import vf.optidepy.model.stored.project.Project

/**
  * Used for reading project deployment configs from the database
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object ProjectDeploymentConfigDbFactory 
	extends CombiningFactory[ProjectDeploymentConfig, DeploymentConfig, Project] with Deprecatable
{
	// IMPLEMENTED	--------------------
	
	override def childFactory = ProjectDbFactory
	
	override def nonDeprecatedCondition = 
		parentFactory.nonDeprecatedCondition && childFactory.nonDeprecatedCondition
	
	override def parentFactory = DeploymentConfigDbFactory
	
	override def apply(config: DeploymentConfig, project: Project) = ProjectDeploymentConfig(config, project)
}

