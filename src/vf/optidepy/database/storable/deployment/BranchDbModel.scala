package vf.optidepy.database.storable.deployment

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.model.immutable.Value
import utopia.vault.model.immutable.{DbPropertyDeclaration, Storable}
import utopia.vault.model.template.{FromIdFactory, HasId, HasIdProperty}
import utopia.vault.nosql.storable.StorableFactory
import utopia.vault.nosql.storable.deprecation.DeprecatableAfter
import vf.optidepy.database.OptidepyTables
import vf.optidepy.model.factory.deployment.BranchFactory
import vf.optidepy.model.partial.deployment.BranchData
import vf.optidepy.model.stored.deployment.Branch

import java.time.Instant

/**
  * Used for constructing BranchDbModel instances and for inserting branches to the database
  * @author Mikko Hilpinen
  * @since 23.08.2024, v1.2
  */
object BranchDbModel 
	extends StorableFactory[BranchDbModel, Branch, BranchData] with FromIdFactory[Int, BranchDbModel] 
		with HasIdProperty with BranchFactory[BranchDbModel] with DeprecatableAfter[BranchDbModel]
{
	// ATTRIBUTES	--------------------
	
	override lazy val id = DbPropertyDeclaration("id", index)
	
	/**
	  * Database property used for interacting with deployment config ids
	  */
	lazy val deploymentConfigId = property("deploymentConfigId")
	
	/**
	  * Database property used for interacting with names
	  */
	lazy val name = property("name")
	
	/**
	  * Database property used for interacting with creation times
	  */
	lazy val created = property("created")
	
	/**
	  * Database property used for interacting with deprecation times
	  */
	lazy val deprecatedAfter = property("deprecatedAfter")
	
	/**
	  * Database property used for interacting with are defaults
	  */
	lazy val isDefault = property("isDefault")
	
	
	// IMPLEMENTED	--------------------
	
	override def table = OptidepyTables.branch
	
	override def apply(data: BranchData) = 
		apply(None, Some(data.deploymentConfigId), data.name, Some(data.created), data.deprecatedAfter, 
			Some(data.isDefault))
	
	/**
	  * @param created Time when this branch was introduced
	  * @return A model containing only the specified created
	  */
	override def withCreated(created: Instant) = apply(created = Some(created))
	
	/**
	  * @param deploymentConfigId Id of the deployment configuration which this branch versions
	  * @return A model containing only the specified deployment config id
	  */
	override def withDeploymentConfigId(deploymentConfigId: Int) = 
		apply(deploymentConfigId = Some(deploymentConfigId))
	
	/**
	  * @param deprecatedAfter Time when this branch was removed
	  * @return A model containing only the specified deprecated after
	  */
	override
		 def withDeprecatedAfter(deprecatedAfter: Instant) = apply(deprecatedAfter = Some(deprecatedAfter))
	
	override def withId(id: Int) = apply(id = Some(id))
	
	/**
	  * @param isDefault Whether this is the default branch of the associated project
	  * @return A model containing only the specified is default
	  */
	override def withIsDefault(isDefault: Boolean) = apply(isDefault = Some(isDefault))
	
	/**
	  * @param name Name of this branch
	  * @return A model containing only the specified name
	  */
	override def withName(name: String) = apply(name = name)
	
	override protected def complete(id: Value, data: BranchData) = Branch(id.getInt, data)
}

/**
  * Used for interacting with Branches in the database
  * @param id branch database id
  * @author Mikko Hilpinen
  * @since 23.08.2024, v1.2
  */
case class BranchDbModel(id: Option[Int] = None, deploymentConfigId: Option[Int] = None, name: String = "", 
	created: Option[Instant] = None, deprecatedAfter: Option[Instant] = None, 
	isDefault: Option[Boolean] = None) 
	extends Storable with HasId[Option[Int]] with FromIdFactory[Int, BranchDbModel] 
		with BranchFactory[BranchDbModel]
{
	// IMPLEMENTED	--------------------
	
	override def table = BranchDbModel.table
	
	override def valueProperties = 
		Vector(BranchDbModel.id.name -> id, BranchDbModel.deploymentConfigId.name -> deploymentConfigId, 
			BranchDbModel.name.name -> name, BranchDbModel.created.name -> created, 
			BranchDbModel.deprecatedAfter.name -> deprecatedAfter, BranchDbModel.isDefault.name -> isDefault)
	
	/**
	  * @param created Time when this branch was introduced
	  * @return A new copy of this model with the specified created
	  */
	override def withCreated(created: Instant) = copy(created = Some(created))
	
	/**
	  * @param deploymentConfigId Id of the deployment configuration which this branch versions
	  * @return A new copy of this model with the specified deployment config id
	  */
	override def withDeploymentConfigId(deploymentConfigId: Int) = 
		copy(deploymentConfigId = Some(deploymentConfigId))
	
	/**
	  * @param deprecatedAfter Time when this branch was removed
	  * @return A new copy of this model with the specified deprecated after
	  */
	override def withDeprecatedAfter(deprecatedAfter: Instant) = copy(deprecatedAfter = Some(deprecatedAfter))
	
	override def withId(id: Int) = copy(id = Some(id))
	
	/**
	  * @param isDefault Whether this is the default branch of the associated project
	  * @return A new copy of this model with the specified is default
	  */
	override def withIsDefault(isDefault: Boolean) = copy(isDefault = Some(isDefault))
	
	/**
	  * @param name Name of this branch
	  * @return A new copy of this model with the specified name
	  */
	override def withName(name: String) = copy(name = name)
}

