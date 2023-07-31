package vf.optidepy.model

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.factory.FromModelFactory
import utopia.flow.generic.model.immutable.Model
import utopia.flow.generic.model.template.{ModelConvertible, ModelLike, Property}
import utopia.flow.time.TimeExtensions._
import utopia.flow.parse.file.FileExtensions._

import java.nio.file.{Path, Paths}
import scala.util.{Success, Try}

object Project extends FromModelFactory[Project]
{
	override def apply(model: ModelLike[Property]): Try[Project] = {
		val inputOutput = model("output").string match {
			case Some(out) => Success(model("input").string.map { Paths.get(_) } -> Paths.get(out))
			case None => Binding(model("root").getModel).map { b => Some(b.source) -> b.target }
		}
		inputOutput.flatMap { case (input, output) =>
			model("bindings").tryVectorWith { v => Binding(v.getModel) }.map { bindings =>
				apply(model("name").getString, input, output, bindings,
					model("useBuildDir").booleanOr(true), model("deletionEnabled").booleanOr(true))
			}
		}
	}
}

/**
 * Represents a project that is deployed
 * @author Mikko Hilpinen
 * @since 20.3.2023, v0.1
 * @param name Name of this project
 * @param input The root input directory (if applicable)
 * @param output The root output directory
 * @param relativeBindings Bindings that determine, which files are moved to where (relative to the main binding)
 * @param usesBuildDirectories Whether this project uses separate build directories by default (default = true).
 *                             If false, only the "full" directory is updated and no separate build directories will
 *                             be created.
 * @param fileDeletionEnabled Whether automatic file deletion is allowed within this project (default = true).
 *                            If false, all old files must be deleted manually.
 */
case class Project(name: String, input: Option[Path], output: Path, relativeBindings: Vector[Binding],
                   usesBuildDirectories: Boolean = true, fileDeletionEnabled: Boolean = true)
	extends ModelConvertible
{
	// COMPUTED -----------------------
	
	/**
	 * @return The directory that will contain the full project output
	 */
	def fullOutputDirectory = output/"full"
	
	/**
	 * @return Directory bindings where input is absolute and output is relative
	 */
	def sourceCorrectedBindings = input match {
		case Some(i) => relativeBindings.map { _.underSource(i) }
		case None => relativeBindings
	}
	
	
	// IMPLEMENTED  ------------------
	
	override def toModel: Model =
		Model.from("name" -> name, "input" -> input.map { _.toJson }, "output" -> output.toJson,
			"bindings" -> relativeBindings, "useBuildDir" -> usesBuildDirectories,
			"deletionEnabled" -> fileDeletionEnabled)
	
	
	// OTHER    ---------------------
	
	/**
	 * @param deployment Targeted deployment
	 * @return A directory where that deployment should be stored
	 */
	def directoryForDeployment(deployment: Deployment) =
		output/s"build-${ deployment.index }-${deployment.timestamp.toLocalDate.toString}"
}