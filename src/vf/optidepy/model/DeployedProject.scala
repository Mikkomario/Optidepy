package vf.optidepy.model

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.factory.FromModelFactory
import utopia.flow.generic.model.immutable.{Constant, Model}
import utopia.flow.generic.model.template.{ModelConvertible, ModelLike, Property}
import utopia.flow.view.template.Extender

import scala.util.Try

object DeployedProject extends FromModelFactory[DeployedProject]
{
	override def apply(model: ModelLike[Property]): Try[DeployedProject] =
		Project(model).map { project =>
			apply(project, model("deployments").getVector.flatMap { v => Deployment(v.getModel).toOption })
		}
}

/**
 * Combines a project with its deployment information
 * @author Mikko Hilpinen
 * @since 20.3.2023, v0.1
 */
case class DeployedProject(project: Project, deployments: Vector[Deployment] = Vector())
	extends Extender[Project] with ModelConvertible
{
	// COMPUTED ---------------------------
	
	/**
	 * @return The latest deployment of this project
	 */
	def lastDeployment = deployments.lastOption
	
	
	// IMPLEMENTED  ----------------------
	
	override def wrapped: Project = project
	
	
	// OTHER    --------------------------
	
	/**
	 * @param deployment A new deployment of this project
	 * @return A copy of this project with that deployment included
	 */
	def +(deployment: Deployment) = copy(deployments = deployments :+ deployment)
	
	
	// IMPLEMENTED  ---------------------
	
	override def toModel: Model = project.toModel + Constant("deployments", deployments)
}
