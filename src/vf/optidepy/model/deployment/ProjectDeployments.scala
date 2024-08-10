package vf.optidepy.model.deployment

import utopia.flow.collection.CollectionExtensions._
import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.factory.FromModelFactory
import utopia.flow.generic.model.immutable.{Constant, Model, Value}
import utopia.flow.generic.model.mutable.DataType.{ModelType, VectorType}
import utopia.flow.generic.model.template.{ModelConvertible, ModelLike, Property}
import utopia.flow.util.Mutate
import utopia.flow.util.StringExtensions._
import utopia.flow.view.template.Extender

import scala.util.Try

@deprecated("Will be replaced with a new version", "v1.2")
object ProjectDeployments extends FromModelFactory[ProjectDeployments]
{
	// ATTRIBUTES   --------------------
	
	/**
	 * The name of the default branch (by default)
	 */
	val defaultBranchName = "main"
	
	
	// IMPLEMENTED  --------------------
	
	override def apply(model: ModelLike[Property]): Try[ProjectDeployments] =
		// Parses the standard project data first
		ProjectDeploymentConfig(model).map { project =>
			// Next, parses deployment data
			val deployments = model("deployments").castTo(ModelType, VectorType) match {
				// Case: Model input type (expected) => Parses deployments per branch
				case Left(modelValue) =>
					modelValue.model match {
						case Some(depModel) =>
							depModel.propertyMap.view.mapValues { c => deploymentsFromValue(c.value) }.toMap
						case None => Map[String, Vector[Deployment]]()
					}
				// Case: Vector input type (backwards-compatibility) => Parses deployments for the default branch
				case Right(vectorValue) => Map(defaultBranchName -> deploymentsFromValue(vectorValue))
			}
			apply(project, deployments)
		}
	
	
	// OTHERS   --------------------------
	
	private def deploymentsFromValue(vectorValue: Value) =
		vectorValue.getVector.flatMap { v => Deployment(v.getModel).toOption }
}

/**
 * Combines a project with its deployment information
 * @author Mikko Hilpinen
 * @since 20.3.2023, v0.1
 */
@deprecated("Will be replaced with a new version", "v1.2")
case class ProjectDeployments(project: ProjectDeploymentConfig, deployments: Map[String, Vector[Deployment]] = Map())
	extends Extender[ProjectDeploymentConfig] with ModelConvertible
{
	// COMPUTED --------------------------
	
	/**
	 * Index associated with the latest deployment.
	 * None if not deployed yet.
	 */
	lazy val lastDeploymentIndex =
		deployments.valuesIterator.flatten.map { _.index }.maxOption
	
	
	// IMPLEMENTED  ----------------------
	
	override def wrapped: ProjectDeploymentConfig = project
	
	
	// OTHER    --------------------------
	
	/**
	 * @param branch Name of the targeted branch
	 * @return The latest deployment of this project
	 */
	def lastDeploymentOf(branch: String) =
		deployments.getOrElse(branch, Vector.empty).lastOption
	
	/**
	 * @param deployment A new deployment of this project as a tuple
	 *                   where the first value is the deployed branch name
	 * @return A copy of this project with that deployment included
	 */
	def +(deployment: (String, Deployment)) =
		copy(deployments = deployments.appendOrMerge(deployment._1, Vector(deployment._2)) { _ ++ _ })
	
	/**
	 * @param f A mapping function to apply to the wrapped project
	 * @return A modified copy of this project
	 */
	def modify(f: Mutate[ProjectDeploymentConfig]) = copy(project = f(project))
	
	
	// IMPLEMENTED  ---------------------
	
	override def toModel: Model = project.toModel +
		Constant("deployments", Model.withConstants(deployments.map { case (branch, deployments) =>
			Constant(branch.nonEmptyOrElse(ProjectDeployments.defaultBranchName), deployments) }))
}
