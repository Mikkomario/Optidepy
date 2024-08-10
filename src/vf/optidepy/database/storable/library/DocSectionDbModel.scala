package vf.optidepy.database.storable.library

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.model.immutable.Value
import utopia.vault.model.immutable.{DbPropertyDeclaration, Storable}
import utopia.vault.model.template.{FromIdFactory, HasIdProperty}
import utopia.vault.nosql.storable.StorableFactory
import vf.optidepy.database.OptidepyTables
import vf.optidepy.model.factory.library.DocSectionFactory
import vf.optidepy.model.partial.library.DocSectionData
import vf.optidepy.model.stored.library.DocSection

import java.time.Instant

/**
  * Used for constructing DocSectionDbModel instances and for inserting doc sections to the database
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DocSectionDbModel 
	extends StorableFactory[DocSectionDbModel, DocSection, DocSectionData] 
		with FromIdFactory[Int, DocSectionDbModel] with DocSectionFactory[DocSectionDbModel] with HasIdProperty
{
	// ATTRIBUTES	--------------------
	
	override lazy val id = DbPropertyDeclaration("id", index)
	
	/**
	  * Database property used for interacting with header ids
	  */
	lazy val headerId = property("headerId")
	
	/**
	  * Database property used for interacting with creation times
	  */
	lazy val created = property("created")
	
	
	// IMPLEMENTED	--------------------
	
	override def table = OptidepyTables.docSection
	
	override def apply(data: DocSectionData) = apply(None, Some(data.headerId), Some(data.created))
	
	/**
	  * @param created Time when this section (version) was added
	  * @return A model containing only the specified created
	  */
	override def withCreated(created: Instant) = apply(created = Some(created))
	
	/**
	  * @param headerId Id of the header (text) of this section.
	  * @return A model containing only the specified header id
	  */
	override def withHeaderId(headerId: Int) = apply(headerId = Some(headerId))
	
	override def withId(id: Int) = apply(id = Some(id))
	
	override protected def complete(id: Value, data: DocSectionData) = DocSection(id.getInt, data)
}

/**
  * Used for interacting with DocSections in the database
  * @param id doc section database id
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DocSectionDbModel(id: Option[Int] = None, headerId: Option[Int] = None, 
	created: Option[Instant] = None) 
	extends Storable with FromIdFactory[Int, DocSectionDbModel] with DocSectionFactory[DocSectionDbModel]
{
	// IMPLEMENTED	--------------------
	
	override def table = DocSectionDbModel.table
	
	override def valueProperties = 
		Vector(DocSectionDbModel.id.name -> id, DocSectionDbModel.headerId.name -> headerId, 
			DocSectionDbModel.created.name -> created)
	
	/**
	  * @param created Time when this section (version) was added
	  * @return A new copy of this model with the specified created
	  */
	override def withCreated(created: Instant) = copy(created = Some(created))
	
	/**
	  * @param headerId Id of the header (text) of this section.
	  * @return A new copy of this model with the specified header id
	  */
	override def withHeaderId(headerId: Int) = copy(headerId = Some(headerId))
	
	override def withId(id: Int) = copy(id = Some(id))
}

