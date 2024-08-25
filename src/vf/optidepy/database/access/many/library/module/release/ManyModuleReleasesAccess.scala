package vf.optidepy.database.access.many.library.module.release

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.util.Version
import utopia.vault.database.Connection
import utopia.vault.nosql.access.many.model.ManyRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.{FilterableView, ViewFactory}
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.ModuleReleaseDbFactory
import vf.optidepy.database.storable.library.ModuleReleaseDbModel
import vf.optidepy.model.stored.library.ModuleRelease

object ManyModuleReleasesAccess extends ViewFactory[ManyModuleReleasesAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): ManyModuleReleasesAccess = 
		_ManyModuleReleasesAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _ManyModuleReleasesAccess(override val accessCondition: Option[Condition]) 
		extends ManyModuleReleasesAccess
}

/**
  * A common trait for access points which target multiple module releases at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait ManyModuleReleasesAccess 
	extends ManyRowModelAccess[ModuleRelease] with FilterableView[ManyModuleReleasesAccess] with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * module ids of the accessible module releases
	  */
	def moduleIds(implicit connection: Connection) = pullColumn(model.moduleId.column).map { v => v.getInt }
	/**
	  * versions of the accessible module releases
	  */
	def versions(implicit connection: Connection) = 
		pullColumn(model.version.column).flatMap { _.string }.map { v => Version(v) }
	/**
	  * jar names of the accessible module releases
	  */
	def jarNames(implicit connection: Connection) = pullColumn(model.jarName.column).flatMap { _.string }
	/**
	  * doc ids of the accessible module releases
	  */
	def docIds(implicit connection: Connection) = pullColumn(model.docId.column).flatMap { v => v.int }
	/**
	  * Unique ids of the accessible module releases
	  */
	def ids(implicit connection: Connection) = pullColumn(index).map { v => v.getInt }
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	protected def model = ModuleReleaseDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = ModuleReleaseDbFactory
	override protected def self = this
	
	override def apply(condition: Condition): ManyModuleReleasesAccess = ManyModuleReleasesAccess(condition)
	
	
	// OTHER	--------------------
	
	/**
	  * Updates the doc ids of the targeted module releases
	  * @param newDocId A new doc id to assign
	  * @return Whether any module release was affected
	  */
	def docIds_=(newDocId: Int)(implicit connection: Connection) = putColumn(model.docId.column, newDocId)
	
	/**
	  * @param moduleId module id to target
	  * @return Copy of this access point that only includes module releases with the specified module id
	  */
	def ofModule(moduleId: Int) = filter(model.moduleId.column <=> moduleId)
	/**
	  * @param moduleIds Targeted module ids
	  * @return Copy of this access point that only includes module releases where module id is within the
	  *  specified value set
	  */
	def ofModules(moduleIds: Iterable[Int]) = filter(model.moduleId.column.in(moduleIds))
	
	/**
	  * @param version version to target
	  * @return Copy of this access point that only includes module releases with the specified version
	  */
	def ofVersion(version: Version) = filter(model.version.column <=> version.toString)
	
	/**
	  * @param versions Targeted versions
	  * 
		@return Copy of this access point that only includes module releases where version is within the specified
	  *  value set
	  */
	def ofVersions(versions: Iterable[Version]) = 
		filter(model.version.column.in(versions.map { version => version.toString }))
	
	/**
	  * @param docId doc id to target
	  * @return Copy of this access point that only includes module releases with the specified doc id
	  */
	def withDocumentation(docId: Int) = filter(model.docId.column <=> docId)
	
	/**
	  * @param docIds Targeted doc ids
	  * @return Copy of this access point that only includes module releases where doc id is within the
	  *  specified value set
	  */
	def withDocumentations(docIds: Iterable[Int]) = filter(model.docId.column.in(docIds))
}

