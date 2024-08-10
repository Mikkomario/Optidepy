package vf.optidepy.model.stored.project

import utopia.flow.generic.model.template.ModelLike.AnyModel
import utopia.vault.model.template.{FromIdFactory, StoredFromModelFactory, StoredModelConvertible}
import vf.optidepy.database.access.single.project.DbSingleProject
import vf.optidepy.model.factory.project.ProjectFactoryWrapper
import vf.optidepy.model.partial.project.ProjectData

object Project extends StoredFromModelFactory[ProjectData, Project]
{
	// IMPLEMENTED	--------------------
	
	override def dataFactory = ProjectData
	
	override protected def complete(model: AnyModel, data: ProjectData) = 
		model("id").tryInt.map { apply(_, data) }
}

/**
  * Represents a project that has already been stored in the database
  * @param id id of this project in the database
  * @param data Wrapped project data
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class Project(id: Int, data: ProjectData) 
	extends StoredModelConvertible[ProjectData] with FromIdFactory[Int, Project] 
		with ProjectFactoryWrapper[ProjectData, Project]
{
	// COMPUTED	--------------------
	
	/**
	  * An access point to this project in the database
	  */
	def access = DbSingleProject(id)
	
	
	// IMPLEMENTED	--------------------
	
	override protected def wrappedFactory = data
	
	override def withId(id: Int) = copy(id = id)
	
	override protected def wrap(data: ProjectData) = copy(data = data)
}

