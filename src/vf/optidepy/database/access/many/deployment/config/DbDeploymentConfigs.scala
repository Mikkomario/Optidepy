package vf.optidepy.database.access.many.deployment.config

import utopia.flow.collection.immutable.Empty
import utopia.vault.database.Connection
import utopia.vault.nosql.view.{NonDeprecatedView, UnconditionalView, ViewManyByIntIds}
import vf.optidepy.database.storable.deployment.BindingDbModel
import vf.optidepy.model.cached.deployment.NewDeploymentConfig
import vf.optidepy.model.stored.deployment.DeploymentConfig

/**
  * The root access point when targeting multiple deployment configs at a time
  * @author Mikko Hilpinen
  * @since 12.08.2024, v1.2
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
	
	private def bindingModel = BindingDbModel
	
	
	// OTHER    --------------------
	
	/**
	 * Inserts 0-n new deployment configurations
	 * @param projectId Id of the project for which these configurations are added
	 * @param configs Configurations to add
	 * @param connection Implicit DB connection
	 * @return Stored configurations
	 */
	def insert(projectId: Int, configs: Seq[NewDeploymentConfig])(implicit connection: Connection) = {
		// Inserts the configurations, so that they can be referenced
		val storedConfigs = model.insertFrom(configs) { _.toDeploymentConfigData(projectId) } {
			(inserted, data) => inserted -> data.bindings }
		// Next inserts the bindings
		val storedBindingsPerConfig = bindingModel
			.insert(storedConfigs.flatMap { case (config, bindings) => bindings.map { _.toBindingData(config.id) } })
			.groupBy { _.configId }.withDefaultValue(Empty)
		
		// Returns combined info
		storedConfigs.map { case (c, _) => c.withBindings(storedBindingsPerConfig(c.id)) }
	}
	
	
	// NESTED	--------------------
	
	object DbDeploymentConfigsIncludingHistory extends ManyDeploymentConfigsAccess with UnconditionalView
}

