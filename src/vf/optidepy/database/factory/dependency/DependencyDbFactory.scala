package vf.optidepy.database.factory.dependency

import utopia.flow.generic.model.immutable.Model
import utopia.flow.parse.file.FileExtensions._
import utopia.vault.nosql.factory.row.model.FromValidatedRowModelFactory
import utopia.vault.nosql.template.Deprecatable
import utopia.vault.sql.OrderBy
import vf.optidepy.database.storable.dependency.DependencyDbModel
import vf.optidepy.model.partial.dependency.DependencyData
import vf.optidepy.model.stored.dependency.Dependency

import java.nio.file.Path

/**
  * Used for reading dependency data from the DB
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DependencyDbFactory extends FromValidatedRowModelFactory[Dependency] with Deprecatable
{
	// COMPUTED	--------------------
	
	/**
	  * Model that specifies the how data is read
	  */
	def model = DependencyDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def defaultOrdering: Option[OrderBy] = None
	
	override def nonDeprecatedCondition = model.nonDeprecatedCondition
	
	override def table = model.table
	
	override protected def fromValidatedModel(valid: Model) = 
		Dependency(valid(this.model.id.name).getInt, 
			DependencyData(valid(this.model.dependentProjectId.name).getInt, 
			valid(this.model.usedModuleId.name).getInt, 
			valid(this.model.relativeLibDirectory.name).getString: Path, 
			valid(this.model.libraryFileName.name).getString, valid(this.model.created.name).getInstant, 
			valid(this.model.deprecatedAfter.name).instant))
}

