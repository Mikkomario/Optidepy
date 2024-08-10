package vf.optidepy.model.library

import utopia.flow.collection.CollectionExtensions._
import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.factory.FromModelFactoryWithSchema
import utopia.flow.generic.model.immutable.{Model, ModelDeclaration}
import utopia.flow.generic.model.mutable.DataType.StringType
import utopia.flow.generic.model.template.ModelConvertible
import utopia.flow.time.Now
import utopia.flow.util.Version
import vf.optidepy.util.Common._

import java.time.Instant

@deprecated("Will be replaced with the new models", "v1.2")
object ModuleRelease extends FromModelFactoryWithSchema[ModuleRelease]
{
	// ATTRIBUTES   -----------------------
	
	override lazy val  schema: ModelDeclaration = ModelDeclaration("version" -> StringType)
	
	
	// IMPLEMENTED  -----------------------
	
	override protected def fromValidatedModel(model: Model): ModuleRelease = {
		val doc = model("doc").model match {
			case Some(model) => DocSection(model).getOrElseLog { DocSection.empty }
			case None => DocSection.empty
		}
		apply(Version(model("version").getString), doc, model("timestamp").getInstant)
	}
}

/**
 * Represents an export / release of a (library) module
 * @param version Released version
 * @param changeDoc Documentation of this version's changes
 * @param timestamp Time when this release was created
 * @author Mikko Hilpinen
 * @since 15.04.2024, v1.2
 */
@deprecated("Will be replaced with the new models", "v1.2")
case class ModuleRelease(version: Version, changeDoc: DocSection = DocSection.empty, timestamp: Instant = Now)
	extends ModelConvertible
{
	override def toModel: Model = Model.from(
		"version" -> version.toString, "doc" -> changeDoc, "timestamp" -> timestamp)
}