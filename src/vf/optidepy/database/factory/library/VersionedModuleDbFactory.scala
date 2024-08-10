package vf.optidepy.database.factory.library

import utopia.flow.generic.model.immutable.Model
import utopia.flow.parse.file.FileExtensions._
import utopia.vault.nosql.factory.row.FromTimelineRowFactory
import utopia.vault.nosql.factory.row.model.FromValidatedRowModelFactory
import utopia.vault.nosql.template.Deprecatable
import vf.optidepy.database.storable.library.VersionedModuleDbModel
import vf.optidepy.model.partial.library.VersionedModuleData
import vf.optidepy.model.stored.library.VersionedModule

import java.nio.file.Path

/**
  * Used for reading versioned module data from the DB
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object VersionedModuleDbFactory 
	extends FromValidatedRowModelFactory[VersionedModule] with FromTimelineRowFactory[VersionedModule] 
		with Deprecatable
{
	// COMPUTED	--------------------
	
	/**
	  * Model that specifies the how data is read
	  */
	def model = VersionedModuleDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def nonDeprecatedCondition = model.nonDeprecatedCondition
	
	override def table = model.table
	
	override def timestamp = model.created
	
	override protected def fromValidatedModel(valid: Model) = 
		VersionedModule(valid(this.model.id.name).getInt, 
			VersionedModuleData(valid(this.model.projectId.name).getInt, 
			valid(this.model.name.name).getString, 
			valid(this.model.relativeChangeListPath.name).getString: Path, 
			valid(this.model.relativeArtifactDirectory.name).getString: Path, 
			valid(this.model.created.name).getInstant, valid(this.model.deprecatedAfter.name).instant))
}

