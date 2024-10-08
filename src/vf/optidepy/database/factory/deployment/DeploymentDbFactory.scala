package vf.optidepy.database.factory.deployment

import utopia.flow.generic.model.immutable.Model
import utopia.flow.util.Version
import utopia.vault.nosql.factory.row.FromTimelineRowFactory
import utopia.vault.nosql.factory.row.model.FromValidatedRowModelFactory
import utopia.vault.nosql.template.Deprecatable
import vf.optidepy.database.storable.deployment.DeploymentDbModel
import vf.optidepy.model.partial.deployment.DeploymentData
import vf.optidepy.model.stored.deployment.Deployment

/**
  * Used for reading deployment data from the DB
  * @author Mikko Hilpinen
  * @since 24.08.2024, v1.2
  */
object DeploymentDbFactory 
	extends FromValidatedRowModelFactory[Deployment] with FromTimelineRowFactory[Deployment] with Deprecatable
{
	// COMPUTED	--------------------
	
	/**
	  * Model that specifies the how data is read
	  */
	def model = DeploymentDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def nonDeprecatedCondition = model.nonDeprecatedCondition
	
	override def table = model.table
	
	override def timestamp = model.created
	
	override protected def fromValidatedModel(valid: Model) = 
		Deployment(valid(this.model.id.name).getInt, DeploymentData(valid(this.model.branchId.name).getInt, 
			valid(this.model.deploymentIndex.name).getInt, 
			Some(Version(valid(this.model.version.name).getString)), 
			valid(this.model.created.name).getInstant, valid(this.model.latestUntil.name).instant))
}

