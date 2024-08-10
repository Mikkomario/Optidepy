package vf.optidepy.model.partial.library

import utopia.flow.collection.immutable.Empty
import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.factory.FromModelFactoryWithSchema
import utopia.flow.generic.model.immutable.{Model, ModelDeclaration, PropertyDeclaration}
import utopia.flow.generic.model.mutable.DataType.InstantType
import utopia.flow.generic.model.mutable.DataType.IntType
import utopia.flow.generic.model.mutable.DataType.StringType
import utopia.flow.generic.model.template.ModelConvertible
import utopia.flow.time.Now
import vf.optidepy.model.factory.library.DocTextFactory

import java.time.Instant

object DocTextData extends FromModelFactoryWithSchema[DocTextData]
{
	// ATTRIBUTES	--------------------
	
	override lazy val schema = ModelDeclaration(Vector(
		PropertyDeclaration("text", StringType, isOptional = true),
		PropertyDeclaration("indentation", IntType, Empty, 0),
		PropertyDeclaration("created", InstantType, isOptional = true)))
	
	
	// IMPLEMENTED	--------------------
	
	override protected def fromValidatedModel(valid: Model) = 
		DocTextData(valid("text").getString, valid("indentation").getInt, valid("created").getInstant)
}

/**
  * Contains a single piece of line of text which is present in some documentation
  * @param text Wrapped text contents
  * @param indentation Level of indentation applicable to this line / text
  * @param created Time when this text was first introduced
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DocTextData(text: String = "", indentation: Int = 0, created: Instant = Now) 
	extends DocTextFactory[DocTextData] with ModelConvertible
{
	// IMPLEMENTED	--------------------
	
	override def toModel = Model(Vector("text" -> text, "indentation" -> indentation, "created" -> created))
	
	override def withCreated(created: Instant) = copy(created = created)
	override def withIndentation(indentation: Int) = copy(indentation = indentation)
	override def withText(text: String) = copy(text = text)
}

