package vf.optidepy.database.factory.project

import utopia.flow.generic.model.immutable.Model
import utopia.flow.parse.file.FileExtensions._
import utopia.vault.nosql.factory.row.model.FromValidatedRowModelFactory
import utopia.vault.nosql.template.Deprecatable
import utopia.vault.sql.OrderBy
import vf.optidepy.database.storable.project.ProjectDbModel
import vf.optidepy.model.partial.project.ProjectData
import vf.optidepy.model.stored.project.Project

import java.nio.file.Path

/**
  * Used for reading project data from the DB
  * @author Mikko Hilpinen
  * @since 12.08.2024, v1.2
  */
object ProjectDbFactory extends FromValidatedRowModelFactory[Project] with Deprecatable
{
	// COMPUTED	--------------------
	
	/**
	  * Model that specifies the how data is read
	  */
	def model = ProjectDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def defaultOrdering: Option[OrderBy] = None
	
	override def nonDeprecatedCondition = model.nonDeprecatedCondition
	
	override def table = model.table
	
	override protected def fromValidatedModel(valid: Model) = 
		Project(valid(this.model.id.name).getInt, ProjectData(valid(this.model.name.name).getString, 
			valid(this.model.rootPath.name).getString: Path, 
			Some(valid(this.model.relativeIdeaPath.name).getString: Path), 
			valid(this.model.created.name).getInstant, valid(this.model.deprecatedAfter.name).instant))
}

