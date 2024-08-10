package vf.optidepy.model.partial.library

import utopia.flow.collection.immutable.{Pair, Single}
import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.factory.FromModelFactoryWithSchema
import utopia.flow.generic.model.immutable.{Model, ModelDeclaration, PropertyDeclaration}
import utopia.flow.generic.model.mutable.DataType.InstantType
import utopia.flow.generic.model.mutable.DataType.IntType
import utopia.flow.generic.model.template.ModelConvertible
import utopia.flow.time.Now
import vf.optidepy.model.factory.library.DocSectionFactory

import java.time.Instant

object DocSectionData extends FromModelFactoryWithSchema[DocSectionData]
{
	// ATTRIBUTES	--------------------
	
	override lazy val schema = 
		ModelDeclaration(Pair(PropertyDeclaration("headerId", IntType, Single("header_id")), 
			PropertyDeclaration("created", InstantType, isOptional = true)))
	
	
	// IMPLEMENTED	--------------------
	
	override protected def fromValidatedModel(valid: Model) = 
		DocSectionData(valid("headerId").getInt, valid("created").getInstant)
}

/**
  * Represents section within a documentation file
  * @param headerId Id of the header (text) of this section.
  * @param created Time when this section (version) was added
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DocSectionData(headerId: Int, created: Instant = Now) 
	extends DocSectionFactory[DocSectionData] with ModelConvertible
{
	// IMPLEMENTED	--------------------
	
	override def toModel = Model(Pair("headerId" -> headerId, "created" -> created))
	
	override def withCreated(created: Instant) = copy(created = created)
	
	override def withHeaderId(headerId: Int) = copy(headerId = headerId)
}

