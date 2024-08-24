package vf.optidepy.model.partial.library

import utopia.flow.collection.immutable.{Empty, Single}
import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.factory.FromModelFactoryWithSchema
import utopia.flow.generic.model.immutable.{Model, ModelDeclaration, PropertyDeclaration}
import utopia.flow.generic.model.mutable.DataType.IntType
import utopia.flow.generic.model.mutable.DataType.StringType
import utopia.flow.generic.model.template.ModelConvertible
import utopia.flow.util.Version
import vf.optidepy.model.factory.library.ModuleReleaseFactory

object ModuleReleaseData extends FromModelFactoryWithSchema[ModuleReleaseData]
{
	// ATTRIBUTES	--------------------
	
	override lazy val schema = ModelDeclaration(Vector(
		PropertyDeclaration("moduleId", IntType, Single("module_id")),
		PropertyDeclaration("version", StringType, Empty, Version(1).toString),
		PropertyDeclaration("jarName", StringType, Single("jar_name"), isOptional = true),
		PropertyDeclaration("docId", IntType, Single("doc_id"), isOptional = true)))
	
	
	// IMPLEMENTED	--------------------
	
	override protected def fromValidatedModel(valid: Model) = 
		ModuleReleaseData(valid("moduleId").getInt, Version(valid("version").getString), 
			valid("jarName").getString, valid("docId").int)
}

/**
  * Represents a published version / build of a versioned module
  * @param moduleId Id of the released module
  * @param version Released version
  * @param jarName Name of the generated jar file.
  * @param docId Id of the documentation of this release.
 *              None if there is no documentation specific to this version.
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class ModuleReleaseData(moduleId: Int, version: Version = Version(1), jarName: String = "",
                             docId: Option[Int] = None)
	extends ModuleReleaseFactory[ModuleReleaseData] with ModelConvertible
{
	// IMPLEMENTED	--------------------
	
	override def toModel = Model(Vector(
		"moduleId" -> moduleId, "version" -> version.toString, "jarName" -> jarName, "docId" -> docId))
	
	override def withDocId(docId: Int) = copy(docId = Some(docId))
	override def withJarName(jarName: String) = copy(jarName = jarName)
	override def withModuleId(moduleId: Int) = copy(moduleId = moduleId)
	override def withVersion(version: Version) = copy(version = version)
}

