package vf.optidepy.model.stored.library

import utopia.flow.generic.model.template.ModelLike.AnyModel
import utopia.vault.model.template.{FromIdFactory, StoredFromModelFactory, StoredModelConvertible}
import vf.optidepy.database.access.single.library.doc.section.DbSingleDocSection
import vf.optidepy.model.factory.library.DocSectionFactoryWrapper
import vf.optidepy.model.partial.library.DocSectionData

object DocSection extends StoredFromModelFactory[DocSectionData, DocSection]
{
	// IMPLEMENTED	--------------------
	
	override def dataFactory = DocSectionData
	
	override protected def complete(model: AnyModel, data: DocSectionData) = 
		model("id").tryInt.map { apply(_, data) }
}

/**
  * Represents a doc section that has already been stored in the database
  * @param id id of this doc section in the database
  * @param data Wrapped doc section data
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DocSection(id: Int, data: DocSectionData) 
	extends StoredModelConvertible[DocSectionData] with FromIdFactory[Int, DocSection] 
		with DocSectionFactoryWrapper[DocSectionData, DocSection]
{
	// COMPUTED	--------------------
	
	/**
	  * An access point to this doc section in the database
	  */
	def access = DbSingleDocSection(id)
	
	
	// IMPLEMENTED	--------------------
	
	override protected def wrappedFactory = data
	
	override def withId(id: Int) = copy(id = id)
	
	override protected def wrap(data: DocSectionData) = copy(data = data)
}

