package vf.optidepy.database.access.single.library.module.release

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.model.immutable.Value
import utopia.flow.util.Version
import utopia.vault.database.Connection
import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.access.template.model.DistinctModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.{FilterableView, ViewFactory}
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.ModuleReleaseDbFactory
import vf.optidepy.database.storable.library.ModuleReleaseDbModel
import vf.optidepy.model.stored.library.ModuleRelease

object UniqueModuleReleaseAccess extends ViewFactory[UniqueModuleReleaseAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): UniqueModuleReleaseAccess = 
		_UniqueModuleReleaseAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _UniqueModuleReleaseAccess(override val accessCondition: Option[Condition]) 
		extends UniqueModuleReleaseAccess
}

/**
  * A common trait for access points that return individual and distinct module releases.
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait UniqueModuleReleaseAccess 
	extends SingleRowModelAccess[ModuleRelease] 
		with DistinctModelAccess[ModuleRelease, Option[ModuleRelease], Value] 
		with FilterableView[UniqueModuleReleaseAccess] with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * Id of the released module. 
	  * None if no module release (or value) was found.
	  */
	def moduleId(implicit connection: Connection) = pullColumn(model.moduleId.column).int
	
	/**
	  * Released version. 
	  * None if no module release (or value) was found.
	  */
	def version(implicit connection: Connection) = Some(Version(pullColumn(model.version.column).getString))
	
	/**
	  * Name of the generated jar file. 
	  * None if no module release (or value) was found.
	  */
	def jarName(implicit connection: Connection) = pullColumn(model.jarName.column).getString
	
	/**
	  * Id of the documentation of this release. None if there is no documentation specific to this version. 
	  * None if no module release (or value) was found.
	  */
	def docId(implicit connection: Connection) = pullColumn(model.docId.column).int
	
	/**
	  * Unique id of the accessible module release. None if no module release was accessible.
	  */
	def id(implicit connection: Connection) = pullColumn(index).int
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	protected def model = ModuleReleaseDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = ModuleReleaseDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): UniqueModuleReleaseAccess = UniqueModuleReleaseAccess(condition)
	
	
	// OTHER	--------------------
	
	/**
	  * Updates the doc ids of the targeted module releases
	  * @param newDocId A new doc id to assign
	  * @return Whether any module release was affected
	  */
	def docId_=(newDocId: Int)(implicit connection: Connection) = putColumn(model.docId.column, newDocId)
}

