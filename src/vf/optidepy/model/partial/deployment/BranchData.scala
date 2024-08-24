package vf.optidepy.model.partial.deployment

import utopia.flow.collection.immutable.Single
import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.factory.FromModelFactoryWithSchema
import utopia.flow.generic.model.immutable.{Model, ModelDeclaration, PropertyDeclaration}
import utopia.flow.generic.model.mutable.DataType.BooleanType
import utopia.flow.generic.model.mutable.DataType.InstantType
import utopia.flow.generic.model.mutable.DataType.IntType
import utopia.flow.generic.model.mutable.DataType.StringType
import utopia.flow.generic.model.template.ModelConvertible
import utopia.flow.time.Now
import vf.optidepy.model.factory.deployment.BranchFactory

import java.time.Instant

object BranchData extends FromModelFactoryWithSchema[BranchData]
{
	// ATTRIBUTES	--------------------
	
	override lazy val schema = 
		ModelDeclaration(Vector(PropertyDeclaration("deploymentConfigId", IntType, 
			Single("deployment_config_id")), PropertyDeclaration("name", StringType), 
			PropertyDeclaration("created", InstantType, isOptional = true), 
			PropertyDeclaration("deprecatedAfter", InstantType, Single("deprecated_after"), 
			isOptional = true), PropertyDeclaration("isDefault", BooleanType, Single("is_default"), false)))
	
	
	// IMPLEMENTED	--------------------
	
	override protected def fromValidatedModel(valid: Model) = 
		BranchData(valid("deploymentConfigId").getInt, valid("name").getString, valid("created").getInstant, 
			valid("deprecatedAfter").instant, valid("isDefault").getBoolean)
}

/**
  * Represents a versioned branch of a project
  * @param deploymentConfigId Id of the deployment configuration which this branch versions
  * @param name Name of this branch
  * @param created Time when this branch was introduced
  * @param deprecatedAfter Time when this branch was removed
  * @param isDefault Whether this is the default branch of the associated project
  * @author Mikko Hilpinen
  * @since 23.08.2024, v1.2
  */
case class BranchData(deploymentConfigId: Int, name: String, created: Instant = Now, 
	deprecatedAfter: Option[Instant] = None, isDefault: Boolean = false) 
	extends BranchFactory[BranchData] with ModelConvertible
{
	// COMPUTED	--------------------
	
	/**
	  * Whether this branch has already been deprecated
	  */
	def isDeprecated = deprecatedAfter.isDefined
	
	/**
	  * Whether this branch is still valid (not deprecated)
	  */
	def isValid = !isDeprecated
	
	
	// IMPLEMENTED	--------------------
	
	override def toModel = 
		Model(Vector("deploymentConfigId" -> deploymentConfigId, "name" -> name, "created" -> created, 
			"deprecatedAfter" -> deprecatedAfter, "isDefault" -> isDefault))
	
	override def withCreated(created: Instant) = copy(created = created)
	
	override
		 def withDeploymentConfigId(deploymentConfigId: Int) = copy(deploymentConfigId = deploymentConfigId)
	
	override def withDeprecatedAfter(deprecatedAfter: Instant) = copy(deprecatedAfter = Some(deprecatedAfter))
	
	override def withIsDefault(isDefault: Boolean) = copy(isDefault = isDefault)
	
	override def withName(name: String) = copy(name = name)
}

