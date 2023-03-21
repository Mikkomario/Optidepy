package vf.optidepy.model

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.factory.FromModelFactoryWithSchema
import utopia.flow.generic.model.immutable.{Model, ModelDeclaration}
import utopia.flow.generic.model.mutable.DataType.{InstantType, IntType}
import utopia.flow.generic.model.template.ModelConvertible
import utopia.flow.time.Now
import vf.optidepy.controller.IndexCounter

import java.time.Instant

object Deployment extends FromModelFactoryWithSchema[Deployment]
{
	// ATTRIBUTES   ---------------------
	
	override lazy val schema: ModelDeclaration = ModelDeclaration("index" -> IntType, "timestamp" -> InstantType)
	
	
	// IMPLEMENTED  ---------------------
	
	override protected def fromValidatedModel(model: Model): Deployment =
		apply(model("index").getInt, model("timestamp").getInstant)
	
	
	// OTHER    ------------------------
	
	/**
	 * Creates a new deployment instance
	 * @param counter A counter used for determing build indices
	 * @return A new deployment
	 */
	def apply()(implicit counter: IndexCounter): Deployment = apply(counter.next(), Now)
}

/**
 * Represents an individual deployment
 * @author Mikko Hilpinen
 * @since 20.3.2023, v0.1
 */
case class Deployment(index: Int, timestamp: Instant) extends ModelConvertible
{
	override def toModel: Model = Model.from("index" -> index, "timestamp" -> timestamp)
}
