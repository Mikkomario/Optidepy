package vf.optidepy.database.storable.dependency

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.model.immutable.Value
import utopia.vault.model.immutable.{DbPropertyDeclaration, Storable}
import utopia.vault.model.template.{FromIdFactory, HasId, HasIdProperty}
import utopia.vault.nosql.storable.StorableFactory
import vf.optidepy.database.OptidepyTables
import vf.optidepy.model.factory.dependency.DependencyUpdateFactory
import vf.optidepy.model.partial.dependency.DependencyUpdateData
import vf.optidepy.model.stored.dependency.DependencyUpdate

import java.time.Instant

/**
  * Used for constructing DependencyUpdateDbModel instances and for inserting dependency updates to the database
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DependencyUpdateDbModel 
	extends StorableFactory[DependencyUpdateDbModel, DependencyUpdate, DependencyUpdateData] 
		with FromIdFactory[Int, DependencyUpdateDbModel] with DependencyUpdateFactory[DependencyUpdateDbModel]
		with HasIdProperty
{
	// ATTRIBUTES	--------------------
	
	override lazy val id = DbPropertyDeclaration("id", index)
	
	/**
	  * Database property used for interacting with dependency ids
	  */
	lazy val dependencyId = property("dependencyId")
	
	/**
	  * Database property used for interacting with release ids
	  */
	lazy val releaseId = property("releaseId")
	
	/**
	  * Database property used for interacting with creation times
	  */
	lazy val created = property("created")
	
	
	// IMPLEMENTED	--------------------
	
	override def table = OptidepyTables.dependencyUpdate
	
	override def apply(data: DependencyUpdateData) = 
		apply(None, Some(data.dependencyId), Some(data.releaseId), Some(data.created))
	
	/**
	  * @param created Time when this update was made
	  * @return A model containing only the specified created
	  */
	override def withCreated(created: Instant) = apply(created = Some(created))
	
	/**
	  * @param dependencyId Id of the dependency this update concerns
	  * @return A model containing only the specified dependency id
	  */
	override def withDependencyId(dependencyId: Int) = apply(dependencyId = Some(dependencyId))
	
	override def withId(id: Int) = apply(id = Some(id))
	
	/**
	  * @param releaseId Id of the library release that was to the parent project
	  * @return A model containing only the specified release id
	  */
	override def withReleaseId(releaseId: Int) = apply(releaseId = Some(releaseId))
	
	override protected def complete(id: Value, data: DependencyUpdateData) = DependencyUpdate(id.getInt, data)
}

/**
  * Used for interacting with DependencyUpdates in the database
  * @param id dependency update database id
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DependencyUpdateDbModel(id: Option[Int] = None, dependencyId: Option[Int] = None, 
	releaseId: Option[Int] = None, created: Option[Instant] = None) 
	extends Storable with FromIdFactory[Int, DependencyUpdateDbModel] 
		with DependencyUpdateFactory[DependencyUpdateDbModel] with HasId[Option[Int]]
{
	// IMPLEMENTED	--------------------
	
	override def table = DependencyUpdateDbModel.table
	
	override def valueProperties = 
		Vector(DependencyUpdateDbModel.id.name -> id, 
			DependencyUpdateDbModel.dependencyId.name -> dependencyId, 
			DependencyUpdateDbModel.releaseId.name -> releaseId, 
			DependencyUpdateDbModel.created.name -> created)
	
	/**
	  * @param created Time when this update was made
	  * @return A new copy of this model with the specified created
	  */
	override def withCreated(created: Instant) = copy(created = Some(created))
	
	/**
	  * @param dependencyId Id of the dependency this update concerns
	  * @return A new copy of this model with the specified dependency id
	  */
	override def withDependencyId(dependencyId: Int) = copy(dependencyId = Some(dependencyId))
	
	override def withId(id: Int) = copy(id = Some(id))
	
	/**
	  * @param releaseId Id of the library release that was to the parent project
	  * @return A new copy of this model with the specified release id
	  */
	override def withReleaseId(releaseId: Int) = copy(releaseId = Some(releaseId))
}

