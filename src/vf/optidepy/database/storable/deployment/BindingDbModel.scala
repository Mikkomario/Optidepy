package vf.optidepy.database.storable.deployment

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.model.immutable.Value
import utopia.flow.parse.file.FileExtensions._
import utopia.vault.model.immutable.{DbPropertyDeclaration, Storable}
import utopia.vault.model.template.{FromIdFactory, HasId, HasIdProperty}
import utopia.vault.nosql.storable.StorableFactory
import vf.optidepy.database.OptidepyTables
import vf.optidepy.model.factory.deployment.BindingFactory
import vf.optidepy.model.partial.deployment.BindingData
import vf.optidepy.model.stored.deployment.Binding

import java.nio.file.Path

/**
  * Used for constructing BindingDbModel instances and for inserting bindings to the database
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object BindingDbModel 
	extends StorableFactory[BindingDbModel, Binding, BindingData] with FromIdFactory[Int, BindingDbModel] 
		with BindingFactory[BindingDbModel] with HasIdProperty
{
	// ATTRIBUTES	--------------------
	
	override lazy val id = DbPropertyDeclaration("id", index)
	
	/**
	  * Database property used for interacting with config ids
	  */
	lazy val configId = property("configId")
	
	/**
	  * Database property used for interacting with sources
	  */
	lazy val source = property("source")
	
	/**
	  * Database property used for interacting with targets
	  */
	lazy val target = property("target")
	
	
	// IMPLEMENTED	--------------------
	
	override def table = OptidepyTables.binding
	
	override def apply(data: BindingData) = 
		apply(None, Some(data.configId), data.source.toJson, data.target.toJson)
	
	/**
	  * @param configId Id of the configuration this binding belongs to
	  * @return A model containing only the specified config id
	  */
	override def withConfigId(configId: Int) = apply(configId = Some(configId))
	
	override def withId(id: Int) = apply(id = Some(id))
	
	/**
	  * 
		@param source Path to the file or directory that is being deployed. Relative to the input root directory.
	  * @return A model containing only the specified source
	  */
	override def withSource(source: Path) = apply(source = source.toJson)
	
	/**
	  * @param target Path to the directory or file where the 'source' is copied. Relative to the root
	  *  output directory.
	  * @return A model containing only the specified target
	  */
	override def withTarget(target: Path) = apply(target = target.toJson)
	
	override def withPaths(source: Path, target: Path): BindingDbModel =
		apply(source = source.toJson, target = target.toJson)
	
	override protected def complete(id: Value, data: BindingData) = Binding(id.getInt, data)
}

/**
  * Used for interacting with Bindings in the database
  * @param id binding database id
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class BindingDbModel(id: Option[Int] = None, configId: Option[Int] = None, source: String = "", target: String = "")
	extends Storable with FromIdFactory[Int, BindingDbModel] with BindingFactory[BindingDbModel] with HasId[Option[Int]]
{
	// IMPLEMENTED	--------------------
	
	override def table = BindingDbModel.table
	
	override def valueProperties = 
		Vector(BindingDbModel.id.name -> id, BindingDbModel.configId.name -> configId, 
			BindingDbModel.source.name -> source, BindingDbModel.target.name -> target)
	
	/**
	  * @param configId Id of the configuration this binding belongs to
	  * @return A new copy of this model with the specified config id
	  */
	override def withConfigId(configId: Int) = copy(configId = Some(configId))
	
	override def withId(id: Int) = copy(id = Some(id))
	
	/**
	  * @param source Path to the file or directory that is being deployed. Relative to the input root directory.
	  * @return A new copy of this model with the specified source
	  */
	override def withSource(source: Path) = copy(source = source.toJson)
	/**
	  * @param target Path to the directory or file where the 'source' is copied. Relative to the root
	  *  output directory.
	  * @return A new copy of this model with the specified target
	  */
	override def withTarget(target: Path) = copy(target = target.toJson)
	
	override def withPaths(source: Path, target: Path): BindingDbModel =
		copy(source = source.toJson, target = target.toJson)
}

