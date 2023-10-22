package vf.optidepy.model

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.factory.FromModelFactoryWithSchema
import utopia.flow.generic.model.immutable.{Model, ModelDeclaration}
import utopia.flow.generic.model.mutable.DataType.StringType
import utopia.flow.generic.model.template.ModelConvertible
import utopia.flow.parse.file.FileExtensions._

import java.nio.file.Path

object Binding extends FromModelFactoryWithSchema[Binding]
{
	override lazy val schema: ModelDeclaration = ModelDeclaration("source" -> StringType, "target" -> StringType)
	
	override protected def fromValidatedModel(model: Model): Binding =
		apply(model("source").getString, model("target").getString)
}

/**
 * Represents a source-target directory or file -binding
 * @author Mikko Hilpinen
 * @since 20.3.2023, v0.1
 * @param source The directory from which data is read.
 * @param target The directory to which data is moved.
 */
case class Binding(source: Path, target: Path) extends ModelConvertible
{
	// COMPUTED -----------------------
	
	/**
	 * @param sourceDir A new parent source directory
	 * @return A copy of this binding where the current source is placed under the specified source directory
	 */
	def underSource(sourceDir: Path) = copy(source = sourceDir/source)
	/**
	 * @param targetDir A new parent target directory
	 * @return A copy of this binding where the current target is placed under the specified target directory
	 */
	def underTarget(targetDir: Path) = copy(target = targetDir/target)
	
	/**
	 * @param other Another (relative) binding
	 * @return The other binding as an absolute binding (i.e. no longer relative to this one)
	 */
	def /(other: Binding) = Binding(source/other.source, target/other.target)
	/**
	 * @param path A relative path
	 * @return A copy of this binding that targets the specified sub-path
	 */
	def /(path: Path) = Binding(source/path, target/path)
	
	
	// IMPLEMENTED  ----------------------
	
	override def toModel: Model = Model.from("source" -> source.toJson, "target" -> target.toJson)
	
	
	// OTHER    --------------------------
	
	/**
	 * @param f A mapping function to apply to source paths
	 * @return Copy of this binding with a mapped source path
	 */
	def mapSource(f: Path => Path) = copy(source = f(source))
}
