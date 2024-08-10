package vf.optidepy.model.stored.library

import utopia.flow.generic.model.template.ModelLike.AnyModel
import utopia.vault.model.template.{FromIdFactory, StoredFromModelFactory, StoredModelConvertible}
import vf.optidepy.database.access.single.library.doc.text.DbSingleDocText
import vf.optidepy.model.factory.library.DocTextFactoryWrapper
import vf.optidepy.model.partial.library.DocTextData

object DocText extends StoredFromModelFactory[DocTextData, DocText]
{
	// IMPLEMENTED	--------------------
	
	override def dataFactory = DocTextData
	
	override protected def complete(model: AnyModel, data: DocTextData) = 
		model("id").tryInt.map { apply(_, data) }
}

/**
  * Represents a doc text that has already been stored in the database
  * @param id id of this doc text in the database
  * @param data Wrapped doc text data
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DocText(id: Int, data: DocTextData) 
	extends StoredModelConvertible[DocTextData] with FromIdFactory[Int, DocText] 
		with DocTextFactoryWrapper[DocTextData, DocText]
{
	// COMPUTED	--------------------
	
	/**
	  * An access point to this doc text in the database
	  */
	def access = DbSingleDocText(id)
	
	
	// IMPLEMENTED	--------------------
	
	override protected def wrappedFactory = data
	
	override def withId(id: Int) = copy(id = id)
	
	override protected def wrap(data: DocTextData) = copy(data = data)
}

