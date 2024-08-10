package vf.optidepy.model.partial.dependency

import utopia.flow.collection.immutable.Single
import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.factory.FromModelFactoryWithSchema
import utopia.flow.generic.model.immutable.{Model, ModelDeclaration, PropertyDeclaration}
import utopia.flow.generic.model.mutable.DataType.InstantType
import utopia.flow.generic.model.mutable.DataType.IntType
import utopia.flow.generic.model.template.ModelConvertible
import utopia.flow.time.Now
import vf.optidepy.model.factory.dependency.DependencyUpdateFactory

import java.time.Instant

object DependencyUpdateData extends FromModelFactoryWithSchema[DependencyUpdateData]
{
	// ATTRIBUTES	--------------------
	
	override lazy val schema = 
		ModelDeclaration(Vector(PropertyDeclaration("dependencyId", IntType, Single("dependency_id")), 
			PropertyDeclaration("releaseId", IntType, Single("release_id")), PropertyDeclaration("created", 
			InstantType, isOptional = true)))
	
	
	// IMPLEMENTED	--------------------
	
	override protected def fromValidatedModel(valid: Model) = 
		DependencyUpdateData(valid("dependencyId").getInt, valid("releaseId").getInt, 
			valid("created").getInstant)
}

/**
  * Represents an event where a project's dependency is updated to a new version.
  * @param dependencyId Id of the dependency this update concerns
  * @param releaseId Id of the library release that was to the parent project
  * @param created Time when this update was made
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DependencyUpdateData(dependencyId: Int, releaseId: Int, created: Instant = Now) 
	extends DependencyUpdateFactory[DependencyUpdateData] with ModelConvertible
{
	// IMPLEMENTED	--------------------
	
	override def toModel = 
		Model(Vector("dependencyId" -> dependencyId, "releaseId" -> releaseId, "created" -> created))
	
	override def withCreated(created: Instant) = copy(created = created)
	
	override def withDependencyId(dependencyId: Int) = copy(dependencyId = dependencyId)
	
	override def withReleaseId(releaseId: Int) = copy(releaseId = releaseId)
}

