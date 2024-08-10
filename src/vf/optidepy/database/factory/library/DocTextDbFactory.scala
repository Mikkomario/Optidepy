package vf.optidepy.database.factory.library

import utopia.flow.generic.model.immutable.Model
import utopia.vault.nosql.factory.row.model.FromValidatedRowModelFactory
import utopia.vault.sql.OrderBy
import vf.optidepy.database.storable.library.DocTextDbModel
import vf.optidepy.model.partial.library.DocTextData
import vf.optidepy.model.stored.library.DocText

/**
  * Used for reading doc text data from the DB
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DocTextDbFactory extends FromValidatedRowModelFactory[DocText]
{
	// COMPUTED	--------------------
	
	/**
	  * Model that specifies the how data is read
	  */
	def model = DocTextDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def defaultOrdering: Option[OrderBy] = None
	
	override def table = model.table
	
	override protected def fromValidatedModel(valid: Model) = 
		DocText(valid(this.model.id.name).getInt, DocTextData(valid(this.model.text.name).getString, 
			valid(this.model.indentation.name).getInt, valid(this.model.created.name).getInstant))
}

