package vf.optidepy.database.factory.dependency

import utopia.flow.generic.model.immutable.Model
import utopia.vault.nosql.factory.row.FromTimelineRowFactory
import utopia.vault.nosql.factory.row.model.FromValidatedRowModelFactory
import vf.optidepy.database.storable.dependency.DependencyUpdateDbModel
import vf.optidepy.model.partial.dependency.DependencyUpdateData
import vf.optidepy.model.stored.dependency.DependencyUpdate

/**
  * Used for reading dependency update data from the DB
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DependencyUpdateDbFactory 
	extends FromValidatedRowModelFactory[DependencyUpdate] with FromTimelineRowFactory[DependencyUpdate]
{
	// COMPUTED	--------------------
	
	/**
	  * Model that specifies the how data is read
	  */
	def model = DependencyUpdateDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def table = model.table
	
	override def timestamp = model.created
	
	override protected def fromValidatedModel(valid: Model) = 
		DependencyUpdate(valid(this.model.id.name).getInt, 
			DependencyUpdateData(valid(this.model.dependencyId.name).getInt, 
			valid(this.model.releaseId.name).getInt, valid(this.model.created.name).getInstant))
}

