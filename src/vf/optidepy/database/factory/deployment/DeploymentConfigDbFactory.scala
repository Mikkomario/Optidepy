package vf.optidepy.database.factory.deployment

import utopia.flow.generic.model.immutable.Model
import utopia.flow.parse.file.FileExtensions._
import utopia.vault.nosql.factory.row.model.FromValidatedRowModelFactory
import utopia.vault.nosql.template.Deprecatable
import utopia.vault.sql.OrderBy
import vf.optidepy.database.storable.deployment.DeploymentConfigDbModel
import vf.optidepy.model.partial.deployment.DeploymentConfigData
import vf.optidepy.model.stored.deployment.DeploymentConfig

import java.nio.file.Path

/**
  * Used for reading deployment config data from the DB
  * @author Mikko Hilpinen
  * @since 12.08.2024, v1.2
  */
object DeploymentConfigDbFactory extends FromValidatedRowModelFactory[DeploymentConfig] with Deprecatable
{
	// COMPUTED	--------------------
	
	/**
	  * Model that specifies the how data is read
	  */
	def model = DeploymentConfigDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def defaultOrdering: Option[OrderBy] = None
	
	override def nonDeprecatedCondition = model.nonDeprecatedCondition
	
	override def table = model.table
	
	override protected def fromValidatedModel(valid: Model) = 
		DeploymentConfig(valid(this.model.id.name).getInt, 
			DeploymentConfigData(valid(this.model.projectId.name).getInt, 
			valid(this.model.outputDirectory.name).getString: Path, 
			valid(this.model.relativeInputDirectory.name).getString: Path, 
			valid(this.model.name.name).getString, valid(this.model.moduleId.name).int, 
			valid(this.model.created.name).getInstant, valid(this.model.deprecatedAfter.name).instant, 
			valid(this.model.usesBuildDirectories.name).getBoolean, 
			valid(this.model.fileDeletionEnabled.name).getBoolean))
}

