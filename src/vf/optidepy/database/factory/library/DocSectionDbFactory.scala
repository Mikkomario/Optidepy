package vf.optidepy.database.factory.library

import utopia.flow.generic.model.immutable.Model
import utopia.vault.nosql.factory.row.model.FromValidatedRowModelFactory
import utopia.vault.sql.OrderBy
import vf.optidepy.database.storable.library.DocSectionDbModel
import vf.optidepy.model.partial.library.DocSectionData
import vf.optidepy.model.stored.library.DocSection

/**
  * Used for reading doc section data from the DB
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DocSectionDbFactory extends FromValidatedRowModelFactory[DocSection]
{
	// COMPUTED	--------------------
	
	/**
	  * Model that specifies the how data is read
	  */
	def model = DocSectionDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def defaultOrdering: Option[OrderBy] = None
	
	override def table = model.table
	
	override protected def fromValidatedModel(valid: Model) = 
		DocSection(valid(this.model.id.name).getInt, DocSectionData(valid(this.model.headerId.name).getInt, 
			valid(this.model.created.name).getInstant))
}

