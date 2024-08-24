package vf.optidepy.database.factory.deployment

import utopia.flow.generic.model.immutable.Model
import utopia.vault.nosql.factory.row.model.FromValidatedRowModelFactory
import utopia.vault.nosql.template.Deprecatable
import utopia.vault.sql.OrderBy
import vf.optidepy.database.storable.deployment.BranchDbModel
import vf.optidepy.model.partial.deployment.BranchData
import vf.optidepy.model.stored.deployment.Branch

/**
  * Used for reading branch data from the DB
  * @author Mikko Hilpinen
  * @since 23.08.2024, v1.2
  */
object BranchDbFactory extends FromValidatedRowModelFactory[Branch] with Deprecatable
{
	// COMPUTED	--------------------
	
	/**
	  * Model that specifies the how data is read
	  */
	def model = BranchDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def defaultOrdering: Option[OrderBy] = None
	
	override def nonDeprecatedCondition = model.nonDeprecatedCondition
	
	override def table = model.table
	
	override protected def fromValidatedModel(valid: Model) = 
		Branch(valid(this.model.id.name).getInt, BranchData(valid(this.model.deploymentConfigId.name).getInt, 
			valid(this.model.name.name).getString, valid(this.model.created.name).getInstant, 
			valid(this.model.deprecatedAfter.name).instant, valid(this.model.isDefault.name).getBoolean))
}

