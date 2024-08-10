package vf.optidepy.database.storable.deployment

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.model.immutable.Value
import utopia.flow.util.Version
import utopia.vault.model.immutable.{DbPropertyDeclaration, Storable}
import utopia.vault.model.template.{FromIdFactory, HasIdProperty}
import utopia.vault.nosql.storable.StorableFactory
import vf.optidepy.database.OptidepyTables
import vf.optidepy.model.factory.deployment.DeploymentFactory
import vf.optidepy.model.partial.deployment.DeploymentData
import vf.optidepy.model.stored.deployment.Deployment

import java.time.Instant

/**
  * Used for constructing DeploymentDbModel instances and for inserting deployments to the database
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DeploymentDbModel 
	extends StorableFactory[DeploymentDbModel, Deployment, DeploymentData] 
		with FromIdFactory[Int, DeploymentDbModel] with DeploymentFactory[DeploymentDbModel] with HasIdProperty
{
	// ATTRIBUTES	--------------------
	
	override lazy val id = DbPropertyDeclaration("id", index)
	
	/**
	  * Database property used for interacting with branch ids
	  */
	lazy val branchId = property("branchId")
	
	/**
	  * Database property used for interacting with indices
	  */
	lazy val deploymentIndex = property("index")
	
	/**
	  * Database property used for interacting with creation times
	  */
	lazy val created = property("created")
	
	/**
	  * Database property used for interacting with versions
	  */
	lazy val version = property("version")
	
	
	// IMPLEMENTED	--------------------
	
	override def table = OptidepyTables.deployment
	
	override def apply(data: DeploymentData) =
		apply(None, Some(data.branchId), Some(data.index), Some(data.created), data.version match {
			case Some(version) => version.toString
			case None => ""
		})
	
	/**
	  * @param branchId Id of the branch that was deployed
	  * @return A model containing only the specified branch id
	  */
	override def withBranchId(branchId: Int) = apply(branchId = Some(branchId))
	
	/**
	  * @param created Time when this deployment was made
	  * @return A model containing only the specified created
	  */
	override def withCreated(created: Instant) = apply(created = Some(created))
	
	override def withId(id: Int) = apply(id = Some(id))
	
	/**
	  *
		@param index Ordered index of this deployment. Relative to other deployments targeting the same branch.
	  * @return A model containing only the specified index
	  */
	override def withIndex(index: Int) = apply(deploymentIndex = Some(index))
	
	/**
	  * @param version Deployed project version. None if versioning is not being used.
	  * @return A model containing only the specified version
	  */
	override def withVersion(version: Version) = apply(version = version.toString)
	
	override protected def complete(id: Value, data: DeploymentData) = Deployment(id.getInt, data)
}

/**
  * Used for interacting with Deployments in the database
  * @param id deployment database id
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DeploymentDbModel(id: Option[Int] = None, branchId: Option[Int] = None, deploymentIndex: Option[Int] = None,
                             created: Option[Instant] = None, version: String = "")
	extends Storable with FromIdFactory[Int, DeploymentDbModel] with DeploymentFactory[DeploymentDbModel]
{
	// IMPLEMENTED	--------------------
	
	override def table = DeploymentDbModel.table
	
	override def valueProperties =
		Vector(DeploymentDbModel.id.name -> id, DeploymentDbModel.branchId.name -> branchId,
			DeploymentDbModel.deploymentIndex.name -> deploymentIndex, DeploymentDbModel.created.name -> created,
			DeploymentDbModel.version.name -> version)
	
	/**
	  * @param branchId Id of the branch that was deployed
	  * @return A new copy of this model with the specified branch id
	  */
	override def withBranchId(branchId: Int) = copy(branchId = Some(branchId))
	
	/**
	  * @param created Time when this deployment was made
	  * @return A new copy of this model with the specified created
	  */
	override def withCreated(created: Instant) = copy(created = Some(created))
	
	override def withId(id: Int) = copy(id = Some(id))
	
	/**
	  * @param index Ordered index of this deployment. Relative to other deployments targeting the same branch.
	  * @return A new copy of this model with the specified index
	  */
	override def withIndex(index: Int) = copy(deploymentIndex = Some(index))
	
	/**
	  * @param version Deployed project version. None if versioning is not being used.
	  * @return A new copy of this model with the specified version
	  */
	override def withVersion(version: Version) = copy(version = version.toString)
}

