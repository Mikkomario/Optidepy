package vf.optidepy.database.factory.library

import utopia.flow.generic.model.immutable.Model
import utopia.flow.util.Version
import utopia.vault.nosql.factory.row.model.FromValidatedRowModelFactory
import utopia.vault.sql.OrderBy
import vf.optidepy.database.storable.library.ModuleReleaseDbModel
import vf.optidepy.model.partial.library.ModuleReleaseData
import vf.optidepy.model.stored.library.ModuleRelease

/**
  * Used for reading module release data from the DB
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object ModuleReleaseDbFactory extends FromValidatedRowModelFactory[ModuleRelease]
{
	// COMPUTED	--------------------
	
	/**
	  * Model that specifies the how data is read
	  */
	def model = ModuleReleaseDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def defaultOrdering: Option[OrderBy] = None
	
	override def table = model.table
	
	override protected def fromValidatedModel(valid: Model) = 
		ModuleRelease(valid(this.model.id.name).getInt, 
			ModuleReleaseData(valid(this.model.moduleId.name).getInt, 
			Version(valid(this.model.version.name).getString), valid(this.model.jarName.name).getString, 
			valid(this.model.docId.name).int))
}

