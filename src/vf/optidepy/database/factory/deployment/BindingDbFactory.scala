package vf.optidepy.database.factory.deployment

import utopia.flow.generic.model.immutable.Model
import utopia.flow.parse.file.FileExtensions._
import utopia.vault.nosql.factory.row.model.FromValidatedRowModelFactory
import utopia.vault.sql.OrderBy
import vf.optidepy.database.storable.deployment.BindingDbModel
import vf.optidepy.model.partial.deployment.BindingData
import vf.optidepy.model.stored.deployment.Binding

import java.nio.file.Path

/**
  * Used for reading binding data from the DB
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object BindingDbFactory extends FromValidatedRowModelFactory[Binding]
{
	// COMPUTED	--------------------
	
	/**
	  * Model that specifies the how data is read
	  */
	def model = BindingDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def defaultOrdering: Option[OrderBy] = None
	
	override def table = model.table
	
	override protected def fromValidatedModel(valid: Model) = 
		Binding(valid(this.model.id.name).getInt, BindingData(valid(this.model.configId.name).getInt, 
			valid(this.model.source.name).getString: Path, valid(this.model.target.name).getString: Path))
}

