package vf.optidepy.model.partial.deployment

import utopia.flow.collection.immutable.Single
import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.factory.FromModelFactoryWithSchema
import utopia.flow.generic.model.immutable.{Model, ModelDeclaration, PropertyDeclaration}
import utopia.flow.generic.model.mutable.DataType.IntType
import utopia.flow.generic.model.mutable.DataType.StringType
import utopia.flow.generic.model.template.ModelConvertible
import utopia.flow.parse.file.FileExtensions._
import vf.optidepy.model.factory.deployment.BindingFactory

import java.nio.file.Path

object BindingData extends FromModelFactoryWithSchema[BindingData]
{
	// ATTRIBUTES	--------------------
	
	override lazy val schema = 
		ModelDeclaration(Vector(PropertyDeclaration("configId", IntType, Single("config_id")), 
			PropertyDeclaration("source", StringType), PropertyDeclaration("target", StringType)))
	
	
	// IMPLEMENTED	--------------------
	
	override protected def fromValidatedModel(valid: Model) = 
		BindingData(valid("configId").getInt, valid("source").getString: Path, 
			valid("target").getString: Path)
}

/**
  * Represents a binding between an input directory/file and an output directory/file
  * @param configId Id of the configuration this binding belongs to
  * @param source Path to the file or directory that is being deployed. Relative to the input root directory.
  * @param target Path to the directory or file where the 'source' is copied. Relative to the root
  *  output directory.
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class BindingData(configId: Int, source: Path, target: Path) 
	extends BindingFactory[BindingData] with ModelConvertible
{
	// IMPLEMENTED	--------------------
	
	override def toModel = 
		Model(Vector("configId" -> configId, "source" -> source.toJson, "target" -> target.toJson))
	
	override def withConfigId(configId: Int) = copy(configId = configId)
	
	override def withSource(source: Path) = copy(source = source)
	
	override def withTarget(target: Path) = copy(target = target)
}

