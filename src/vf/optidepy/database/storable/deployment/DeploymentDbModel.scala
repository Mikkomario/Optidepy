package vf.optidepy.database.storable.deployment

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.model.immutable.Value
import utopia.flow.util.Version
import utopia.vault.model.immutable.{DbPropertyDeclaration, Storable}
import utopia.vault.model.template.{FromIdFactory, HasId, HasIdProperty}
import utopia.vault.nosql.storable.StorableFactory
import utopia.vault.nosql.storable.deprecation.NullDeprecatable
import vf.optidepy.database.OptidepyTables
import vf.optidepy.model.factory.deployment.DeploymentFactory
import vf.optidepy.model.partial.deployment.DeploymentData
import vf.optidepy.model.stored.deployment.Deployment

import java.time.Instant

/**
  * Used for constructing DeploymentDbModel instances and for inserting deployments to the database
  * @author Mikko Hilpinen
  * @since 24.08.2024, v1.2
  */
object DeploymentDbModel 
	extends StorableFactory[DeploymentDbModel, Deployment, DeploymentData] 
		with FromIdFactory[Int, DeploymentDbModel] with HasIdProperty 
		with DeploymentFactory[DeploymentDbModel] with NullDeprecatable[DeploymentDbModel]
{
	// ATTRIBUTES	--------------------
	
	override lazy val id = DbPropertyDeclaration("id", index)
	
	/**
	  * Database property used for interacting with branch ids
	  */
	lazy val branchId = property("branchId")
	
	/**
	  * Database property used for interacting with deployment indices
	  */
	lazy val deploymentIndex = property("deploymentIndex")
	
	/**
	  * Database property used for interacting with versions
	  */
	lazy val version = property("version")
	
	/**
	  * Database property used for interacting with creation times
	  */
	lazy val created = property("created")
	
	/**
	  * Database property used for interacting with latest untils
	  */
	lazy val latestUntil = property("latestUntil")
	
	override val deprecationAttName = "latestUntil"
	
	
	// IMPLEMENTED	--------------------
	
	override def table = OptidepyTables.deployment
	
	override def apply(data: DeploymentData) = 
		apply(None, Some(data.branchId), Some(data.deploymentIndex),
			data.version match {
				case Some(version) => version.toString
				case None => ""
			},
			Some(data.created), data.latestUntil)
	
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
	
	/**
	  * @param deploymentIndex Ordered index of this deployment. Relative to other deployments targeting the
	  *  same branch.
	  * @return A model containing only the specified deployment index
	  */
	override def withDeploymentIndex(deploymentIndex: Int) = apply(deploymentIndex = Some(deploymentIndex))
	
	override def withDeprecatedAfter(deprecationTime: Instant) = withLatestUntil(deprecationTime)
	
	override def withId(id: Int) = apply(id = Some(id))
	
	/**
	  * @param latestUntil Timestamp after which this was no longer the latest deployment. None while this is
	  *  the latest deployment.
	  * @return A model containing only the specified latest until
	  */
	override def withLatestUntil(latestUntil: Instant) = apply(latestUntil = Some(latestUntil))
	
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
  * @since 24.08.2024, v1.2
  */
case class DeploymentDbModel(id: Option[Int] = None, branchId: Option[Int] = None, 
	deploymentIndex: Option[Int] = None, version: String = "", created: Option[Instant] = None, 
	latestUntil: Option[Instant] = None) 
	extends Storable with HasId[Option[Int]] with FromIdFactory[Int, DeploymentDbModel] 
		with DeploymentFactory[DeploymentDbModel]
{
	// IMPLEMENTED	--------------------
	
	override def table = DeploymentDbModel.table
	
	override def valueProperties = 
		Vector(DeploymentDbModel.id.name -> id, DeploymentDbModel.branchId.name -> branchId, 
			DeploymentDbModel.deploymentIndex.name -> deploymentIndex, 
			DeploymentDbModel.version.name -> version, DeploymentDbModel.created.name -> created, 
			DeploymentDbModel.latestUntil.name -> latestUntil)
	
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
	
	/**
	  * @param deploymentIndex Ordered index of this deployment. Relative to other deployments targeting the
	  *  same branch.
	  * @return A new copy of this model with the specified deployment index
	  */
	override def withDeploymentIndex(deploymentIndex: Int) = copy(deploymentIndex = Some(deploymentIndex))
	
	override def withId(id: Int) = copy(id = Some(id))
	
	/**
	  * @param latestUntil Timestamp after which this was no longer the latest deployment. None while this is
	  *  the latest deployment.
	  * @return A new copy of this model with the specified latest until
	  */
	override def withLatestUntil(latestUntil: Instant) = copy(latestUntil = Some(latestUntil))
	
	/**
	  * @param version Deployed project version. None if versioning is not being used.
	  * @return A new copy of this model with the specified version
	  */
	override def withVersion(version: Version) = copy(version = version.toString)
}

