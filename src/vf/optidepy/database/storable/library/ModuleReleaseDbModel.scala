package vf.optidepy.database.storable.library

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.model.immutable.Value
import utopia.flow.util.Version
import utopia.vault.model.immutable.{DbPropertyDeclaration, Storable}
import utopia.vault.model.template.{FromIdFactory, HasIdProperty}
import utopia.vault.nosql.storable.StorableFactory
import vf.optidepy.database.OptidepyTables
import vf.optidepy.model.factory.library.ModuleReleaseFactory
import vf.optidepy.model.partial.library.ModuleReleaseData
import vf.optidepy.model.stored.library.ModuleRelease

/**
  * Used for constructing ModuleReleaseDbModel instances and for inserting module releases to the database
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object ModuleReleaseDbModel 
	extends StorableFactory[ModuleReleaseDbModel, ModuleRelease, ModuleReleaseData] 
		with FromIdFactory[Int, ModuleReleaseDbModel] with ModuleReleaseFactory[ModuleReleaseDbModel] with HasIdProperty
{
	// ATTRIBUTES	--------------------
	
	override lazy val id = DbPropertyDeclaration("id", index)
	
	/**
	  * Database property used for interacting with module ids
	  */
	lazy val moduleId = property("moduleId")
	
	/**
	  * Database property used for interacting with versions
	  */
	lazy val version = property("version")
	
	/**
	  * Database property used for interacting with jar names
	  */
	lazy val jarName = property("jarName")
	
	/**
	  * Database property used for interacting with doc ids
	  */
	lazy val docId = property("docId")
	
	
	// IMPLEMENTED	--------------------
	
	override def table = OptidepyTables.moduleRelease
	
	override def apply(data: ModuleReleaseData) = 
		apply(None, Some(data.moduleId), data.version.toString, data.jarName, data.docId)
	
	/**
	  * @param docId Id of the documentation of this release. None if there is no documentation specific to
	  *  this version.
	  * @return A model containing only the specified doc id
	  */
	override def withDocId(docId: Int) = apply(docId = Some(docId))
	
	override def withId(id: Int) = apply(id = Some(id))
	
	/**
	  * @param jarName Name of the generated jar file.
	  * @return A model containing only the specified jar name
	  */
	override def withJarName(jarName: String) = apply(jarName = jarName)
	
	/**
	  * @param moduleId Id of the released module
	  * @return A model containing only the specified module id
	  */
	override def withModuleId(moduleId: Int) = apply(moduleId = Some(moduleId))
	
	/**
	  * @param version Released version
	  * @return A model containing only the specified version
	  */
	override def withVersion(version: Version) = apply(version = version.toString)
	
	override protected def complete(id: Value, data: ModuleReleaseData) = ModuleRelease(id.getInt, data)
}

/**
  * Used for interacting with ModuleReleases in the database
  * @param id module release database id
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class ModuleReleaseDbModel(id: Option[Int] = None, moduleId: Option[Int] = None, version: String = "", 
	jarName: String = "", docId: Option[Int] = None) 
	extends Storable with FromIdFactory[Int, ModuleReleaseDbModel] 
		with ModuleReleaseFactory[ModuleReleaseDbModel]
{
	// IMPLEMENTED	--------------------
	
	override def table = ModuleReleaseDbModel.table
	
	override def valueProperties = 
		Vector(ModuleReleaseDbModel.id.name -> id, ModuleReleaseDbModel.moduleId.name -> moduleId, 
			ModuleReleaseDbModel.version.name -> version, ModuleReleaseDbModel.jarName.name -> jarName, 
			ModuleReleaseDbModel.docId.name -> docId)
	
	/**
	  * @param docId Id of the documentation of this release. None if there is no documentation specific to
	  *  this version.
	  * @return A new copy of this model with the specified doc id
	  */
	override def withDocId(docId: Int) = copy(docId = Some(docId))
	
	override def withId(id: Int) = copy(id = Some(id))
	
	/**
	  * @param jarName Name of the generated jar file.
	  * @return A new copy of this model with the specified jar name
	  */
	override def withJarName(jarName: String) = copy(jarName = jarName)
	
	/**
	  * @param moduleId Id of the released module
	  * @return A new copy of this model with the specified module id
	  */
	override def withModuleId(moduleId: Int) = copy(moduleId = Some(moduleId))
	
	/**
	  * @param version Released version
	  * @return A new copy of this model with the specified version
	  */
	override def withVersion(version: Version) = copy(version = version.toString)
}

