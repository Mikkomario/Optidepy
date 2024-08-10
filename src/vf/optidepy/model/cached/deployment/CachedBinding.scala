package vf.optidepy.model.cached.deployment

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.factory.FromModelFactoryWithSchema
import utopia.flow.generic.model.immutable.{Model, ModelDeclaration}
import utopia.flow.generic.model.mutable.DataType.StringType
import utopia.flow.generic.model.template.ModelConvertible
import utopia.flow.parse.file.FileExtensions._
import vf.optidepy.model.template.deployment.BindingLike

import java.nio.file.Path

object CachedBinding extends FromModelFactoryWithSchema[CachedBinding]
{
	override lazy val schema: ModelDeclaration = ModelDeclaration("source" -> StringType, "target" -> StringType)
	
	override protected def fromValidatedModel(model: Model): CachedBinding =
		apply(model("source").getString, model("target").getString)
}

/**
 * Represents a source-target directory or file -binding
 * @author Mikko Hilpinen
 * @since 20.3.2023, v0.1
 * @param source The directory from which data is read.
 * @param target The directory to which data is moved.
 */
case class CachedBinding(source: Path, target: Path) extends ModelConvertible with BindingLike[CachedBinding]
{
	// IMPLEMENTED  ----------------------
	
	override def toModel: Model = Model.from("source" -> source.toJson, "target" -> target.toJson)
	
	override def withPaths(source: Path, target: Path): CachedBinding = CachedBinding(source, target)
}
