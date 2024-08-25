package vf.optidepy.model.partial.deployment

import utopia.flow.collection.immutable.Single
import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.factory.FromModelFactoryWithSchema
import utopia.flow.generic.model.immutable.{Model, ModelDeclaration, PropertyDeclaration, Value}
import utopia.flow.generic.model.mutable.DataType.InstantType
import utopia.flow.generic.model.mutable.DataType.IntType
import utopia.flow.generic.model.mutable.DataType.StringType
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
			PropertyDeclaration("deploymentIndex", IntType, Single("deployment_index")), 
			PropertyDeclaration("version", StringType, isOptional = true), PropertyDeclaration("created", 
			InstantType, isOptional = true), PropertyDeclaration("latestUntil", InstantType, 
			Single("latest_until"), isOptional = true)))
	
	
	// IMPLEMENTED	--------------------
	
	override protected def fromValidatedModel(valid: Model) = 
		DeploymentData(valid("branchId").getInt, valid("deploymentIndex").getInt, 
			Some(Version(valid("version").getString)), valid("created").getInstant, 
			valid("latestUntil").instant)
			
	
	// OTHER    ------------------------
	
	/**
	 * @param branchId Id of the branch on which this deployment was made
	 * @param indexCounter Implicit index counter used for acquiring the next deployment index
	 * @return New deployment data
	 */
	def apply(branchId: Int)(implicit indexCounter: IndexCounter): DeploymentData = apply(branchId, indexCounter.next())
}

/**
  * Represents an event where a project is deployed
  * @param branchId Id of the branch that was deployed
  * @param deploymentIndex Ordered index of this deployment. Relative to other deployments targeting the
  *  same branch.
  * @param version Deployed project version. None if versioning is not being used.
  * @param created Time when this deployment was made
  * @param latestUntil Timestamp after which this was no longer the latest deployment. None while this is
  *  the latest deployment.
  * @author Mikko Hilpinen
  * @since 24.08.2024, v1.2
  */
case class DeploymentData(branchId: Int, deploymentIndex: Int, version: Option[Version] = None,
                          created: Instant = Now, latestUntil: Option[Instant] = None)
	extends DeploymentFactory[DeploymentData] with ModelConvertible
{
	// COMPUTED	--------------------
	
	/**
	  * Whether this deployment has already been deprecated
	  */
	def isDeprecated = latestUntil.isDefined
	
	/**
	  * Whether this deployment is still valid (not deprecated)
	  */
	def isValid = !isDeprecated
	
	
	// IMPLEMENTED	--------------------
	
	override def toModel = 
		Model(Vector("branchId" -> branchId, "deploymentIndex" -> deploymentIndex, 
			"version" -> (version match { case Some(v) => v.toString; case None => Value.empty }), 
			"created" -> created, "latestUntil" -> latestUntil))
	
	override def withBranchId(branchId: Int) = copy(branchId = branchId)
	
	override def withCreated(created: Instant) = copy(created = created)
	
	override def withDeploymentIndex(deploymentIndex: Int) = copy(deploymentIndex = deploymentIndex)
	
	override def withLatestUntil(latestUntil: Instant) = copy(latestUntil = Some(latestUntil))
	
	override def withVersion(version: Version) = copy(version = Some(version))
}

