package vf.optidepy.model

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.factory.FromModelFactory
import utopia.flow.generic.model.immutable.Model
import utopia.flow.generic.model.template.{ModelConvertible, ModelLike, Property}
import utopia.flow.time.TimeExtensions._
import utopia.flow.parse.file.FileExtensions._

import scala.util.Try

object Project extends FromModelFactory[Project]
{
	override def apply(model: ModelLike[Property]): Try[Project] =
		Binding(model("root").getModel).flatMap { main =>
			model("bindings").tryVectorWith { v => Binding(v.getModel) }.map { bindings =>
				apply(model("name").getString, main, bindings)
			}
		}
}

/**
 * Represents a project that is deployed
 * @author Mikko Hilpinen
 * @since 20.3.2023, v0.1
 * @param name Name of this project
 * @param mainBinding Binding that corresponds to the project input and output root directories
 * @param relativeBindings Bindings that determine, which files are moved to where (relative to the main binding)
 */
case class Project(name: String, mainBinding: Binding, relativeBindings: Vector[Binding]) extends ModelConvertible
{
	// COMPUTED -----------------------
	
	/**
	 * @return The root input directory
	 */
	def input = mainBinding.source
	/**
	 * @return The root output directory
	 */
	def output = mainBinding.target
	
	/**
	 * @return The directory that will contain the full project output
	 */
	def fullOutputDirectory = output/"full"
	
	/**
	 * @return Directory bindings where input is absolute and output is relative
	 */
	def sourceCorrectedBindings = relativeBindings.map { _.underSource(input) }
	
	
	// IMPLEMENTED  ------------------
	
	override def toModel: Model =
		Model.from("name" -> name, "root" -> mainBinding, "bindings" -> relativeBindings)
	
	
	// OTHER    ---------------------
	
	/**
	 * @param deployment Targeted deployment
	 * @return A directory where that deployment should be stored
	 */
	def directoryForDeployment(deployment: Deployment) =
		output/s"build-${ deployment.index }-${deployment.timestamp.toLocalDate.toString}"
}