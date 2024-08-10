package vf.optidepy.database.storable.library

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.model.immutable.Value
import utopia.vault.model.immutable.{DbPropertyDeclaration, Storable}
import utopia.vault.model.template.{FromIdFactory, HasIdProperty}
import utopia.vault.nosql.storable.StorableFactory
import vf.optidepy.database.OptidepyTables
import vf.optidepy.model.factory.library.DocTextFactory
import vf.optidepy.model.partial.library.DocTextData
import vf.optidepy.model.stored.library.DocText

import java.time.Instant

/**
  * Used for constructing DocTextDbModel instances and for inserting doc texts to the database
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DocTextDbModel 
	extends StorableFactory[DocTextDbModel, DocText, DocTextData] with FromIdFactory[Int, DocTextDbModel] 
		with DocTextFactory[DocTextDbModel] with HasIdProperty
{
	// ATTRIBUTES	--------------------
	
	override lazy val id = DbPropertyDeclaration("id", index)
	
	/**
	  * Database property used for interacting with texts
	  */
	lazy val text = property("text")
	
	/**
	  * Database property used for interacting with indentations
	  */
	lazy val indentation = property("indentation")
	
	/**
	  * Database property used for interacting with creation times
	  */
	lazy val created = property("created")
	
	
	// IMPLEMENTED	--------------------
	
	override def table = OptidepyTables.docText
	
	override def apply(data: DocTextData) = apply(None, data.text, Some(data.indentation), Some(data.created))
	
	/**
	  * @param created Time when this text was first introduced
	  * @return A model containing only the specified created
	  */
	override def withCreated(created: Instant) = apply(created = Some(created))
	
	override def withId(id: Int) = apply(id = Some(id))
	
	/**
	  * @param indentation Level of indentation applicable to this line / text
	  * @return A model containing only the specified indentation
	  */
	override def withIndentation(indentation: Int) = apply(indentation = Some(indentation))
	
	/**
	  * @param text Wrapped text contents
	  * @return A model containing only the specified text
	  */
	override def withText(text: String) = apply(text = text)
	
	override protected def complete(id: Value, data: DocTextData) = DocText(id.getInt, data)
}

/**
  * Used for interacting with DocTexts in the database
  * @param id doc text database id
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DocTextDbModel(id: Option[Int] = None, text: String = "", indentation: Option[Int] = None, 
	created: Option[Instant] = None) 
	extends Storable with FromIdFactory[Int, DocTextDbModel] with DocTextFactory[DocTextDbModel]
{
	// IMPLEMENTED	--------------------
	
	override def table = DocTextDbModel.table
	
	override def valueProperties = 
		Vector(DocTextDbModel.id.name -> id, DocTextDbModel.text.name -> text, 
			DocTextDbModel.indentation.name -> indentation, DocTextDbModel.created.name -> created)
	
	/**
	  * @param created Time when this text was first introduced
	  * @return A new copy of this model with the specified created
	  */
	override def withCreated(created: Instant) = copy(created = Some(created))
	
	override def withId(id: Int) = copy(id = Some(id))
	
	/**
	  * @param indentation Level of indentation applicable to this line / text
	  * @return A new copy of this model with the specified indentation
	  */
	override def withIndentation(indentation: Int) = copy(indentation = Some(indentation))
	
	/**
	  * @param text Wrapped text contents
	  * @return A new copy of this model with the specified text
	  */
	override def withText(text: String) = copy(text = text)
}

