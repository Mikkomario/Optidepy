package vf.optidepy.model.deployment

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.factory.FromModelFactoryWithSchema
import utopia.flow.generic.model.immutable.{Model, ModelDeclaration}
import utopia.flow.generic.model.mutable.DataType.{InstantType, IntType}
import utopia.flow.generic.model.template.ModelConvertible
import utopia.flow.time.Now
import utopia.flow.util.Version
import vf.optidepy.controller.IndexCounter

import java.time.Instant

@deprecated("Replaced with a new DeploymentData", "v1.2")
object Deployment extends FromModelFactoryWithSchema[Deployment]
{
	// ATTRIBUTES   ---------------------
	
	override lazy val schema: ModelDeclaration = ModelDeclaration("index" -> IntType, "timestamp" -> InstantType)
	
	
	// IMPLEMENTED  ---------------------
	
	override protected def fromValidatedModel(model: Model): Deployment =
		apply(model("index").getInt, model("timestamp").getInstant, model("version").string.flatMap(Version.findFrom))
	
	
	// OTHER    ------------------------
	
	/**
	 * Creates a new deployment instance
	 * @param counter A counter used for determining build-indices
	 * @return A new deployment
	 */
	def apply()(implicit counter: IndexCounter): Deployment = apply(counter.next(), Now)
	
	/**
	 * Creates a new versioned deployment instance
	 * @param version Deployed version
	 * @param counter A counter for determining build-indices
	 * @return A new deployment
	 */
	def versioned(version: Version)(implicit counter: IndexCounter) =
		apply(counter.next(), Now, Some(version))
}

/**
 * Represents an individual deployment
 * @author Mikko Hilpinen
 * @since 20.3.2023, v0.1
 */
@deprecated("Replaced with a new DeploymentData", "v1.2")
case class Deployment(index: Int, timestamp: Instant, version: Option[Version] = None) extends ModelConvertible
{
	override def toModel: Model = Model.from(
		"index" -> index, "timestamp" -> timestamp, "version" -> version.map { _.toString })
}
