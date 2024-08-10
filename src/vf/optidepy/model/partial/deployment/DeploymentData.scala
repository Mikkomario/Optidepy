package vf.optidepy.model.partial.deployment

import utopia.flow.collection.immutable.Single
import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.factory.FromModelFactoryWithSchema
import utopia.flow.generic.model.immutable.{Model, ModelDeclaration, PropertyDeclaration, Value}
import utopia.flow.generic.model.mutable.DataType.{InstantType, IntType, StringType}
import utopia.flow.generic.model.template.ModelConvertible
import utopia.flow.time.Now
import utopia.flow.util.Version
import vf.optidepy.controller.IndexCounter
import vf.optidepy.model.factory.deployment.DeploymentFactory

import java.time.Instant

object DeploymentData extends FromModelFactoryWithSchema[DeploymentData]
{
	// ATTRIBUTES	--------------------
	
	override lazy val schema = 
		ModelDeclaration(Vector(PropertyDeclaration("branchId", IntType, Single("branch_id")), 
			PropertyDeclaration("index", IntType), PropertyDeclaration("created", InstantType, 
			isOptional = true), PropertyDeclaration("version", StringType, isOptional = true)))
	
	
	// IMPLEMENTED	--------------------
	
	override protected def fromValidatedModel(valid: Model) = 
		DeploymentData(valid("branchId").getInt, valid("index").getInt, valid("created").getInstant, 
			Some(Version(valid("version").getString)))
	
	
	// OTHER    ------------------------
	
	/**
	 * Creates a new deployment instance
	 * @param branchId Id of the branch that was deployed
	 * @param counter A counter used for determining build-indices
	 * @return A new deployment
	 */
	def apply(branchId: Int)(implicit counter: IndexCounter): DeploymentData = apply(branchId, counter.next(), Now)
	
	/**
	 * Creates a new versioned deployment instance
	 * @param branchId Id of the branch that was deployed
	 * @param version Deployed version
	 * @param counter A counter for determining build-indices
	 * @return A new deployment
	 */
	def versioned(branchId: Int, version: Version)(implicit counter: IndexCounter) =
		apply(branchId, counter.next(), Now, Some(version))
}

/**
  * Represents an event where a project is deployed
  * @param branchId Id of the branch that was deployed
  * @param index Ordered index of this deployment. Relative to other deployments targeting the same branch.
  * @param created Time when this deployment was made
  * @param version Deployed project version. None if versioning is not being used.
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DeploymentData(branchId: Int, index: Int, created: Instant = Now, version: Option[Version] = None) 
	extends DeploymentFactory[DeploymentData] with ModelConvertible
{
	// IMPLEMENTED	--------------------
	
	override def toModel = 
		Model(Vector("branchId" -> branchId, "index" -> index, "created" -> created, 
			"version" -> (version match { case Some(v) => v.toString; case None => Value.empty })))
	
	override def withBranchId(branchId: Int) = copy(branchId = branchId)
	override def withCreated(created: Instant) = copy(created = created)
	override def withIndex(index: Int) = copy(index = index)
	override def withVersion(version: Version) = copy(version = Some(version))
}

