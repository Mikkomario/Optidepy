package vf.optidepy.model.deployment

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.factory.FromModelFactory
import utopia.flow.generic.model.immutable.Model
import utopia.flow.generic.model.template.{ModelConvertible, ModelLike, Property}
import utopia.flow.parse.file.FileExtensions._
import utopia.flow.time.TimeExtensions._
import vf.optidepy.model.cached.deployment.CachedBinding

import java.nio.file.{Path, Paths}
import scala.util.{Success, Try}

@deprecated("Will be replaced with the new models", "v1.2")
object ProjectDeploymentConfig extends FromModelFactory[ProjectDeploymentConfig]
{
	override def apply(model: ModelLike[Property]): Try[ProjectDeploymentConfig] = {
		val inputOutput = model("output").string match {
			case Some(out) => Success(model("input").string.map { Paths.get(_) } -> Paths.get(out))
			case None => CachedBinding(model("root").getModel).map { b => Some(b.source) -> b.target }
		}
		inputOutput.flatMap { case (input, output) =>
			model("bindings").tryVectorWith { v => CachedBinding(v.getModel) }.map { bindings =>
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
@deprecated("Will be replaced with the new models", "v1.2")
case class ProjectDeploymentConfig(name: String, input: Option[Path], output: Path,
                                   relativeBindings: Vector[CachedBinding],
                                   usesBuildDirectories: Boolean = true, fileDeletionEnabled: Boolean = true)
	extends ModelConvertible
{
	// COMPUTED -----------------------
	
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
	 * @param branch Name of the targeted branch
	 * @return The directory that will contain the full project output for that branch
	 */
	def fullOutputDirectoryFor(branch: String) = output / branch
	
	/**
	 * @param branch Name of the deployed branch
	 * @param deployment Targeted deployment
	 * @return A directory where that deployment should be stored
	 */
	def directoryForDeployment(branch: String, deployment: Deployment) =
		output/s"$branch-build-${ deployment.index }-${deployment.timestamp.toLocalDate.toString}"
	
	/**
	 * Creates a copy of this project with altered input path.
	 * Bindings are preserved, but relativized to the new path, if possible.
	 * Input bindings that are not relative to the new input path are discarded.
	 * @param newInput New input root
	 * @return Copy of this project with altered input root
	 */
	// TODO: This method needs to be written with the new models, also (check use-cases)
	def withInput(newInput: Option[Path]) = {
		// Transforms the input paths to their absolute form for the conversion
		val absoluteBindings = input match {
			case Some(oldRoot) => relativeBindings.map { _.mapSource { p => (oldRoot / p).absolute } }
			case None => relativeBindings
		}
		// Converts to paths relative to the new root
		// Paths which are not relative to the new root are discarded
		val newBindings = newInput match {
			case Some(newInput) =>
				absoluteBindings.flatMap { b =>
					b.source.relativeTo(newInput).toOption.map { newSource => b.copy(source = newSource) }
				}
			case None => absoluteBindings
		}
		copy(input = newInput, relativeBindings = newBindings)
	}
}