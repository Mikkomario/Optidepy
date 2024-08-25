package vf.optidepy.database.access.single.library.module

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.util.Version
import utopia.vault.database.Connection
import utopia.vault.nosql.access.single.model.SingleChronoRowModelAccess
import utopia.vault.nosql.view.{NullDeprecatableView, ViewFactory}
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.PossiblyReleasedModuleDbFactory
import vf.optidepy.database.storable.library.ModuleReleaseDbModel
import vf.optidepy.model.combined.library.PossiblyReleasedModule

object UniquePossiblyReleasedModuleAccess extends ViewFactory[UniquePossiblyReleasedModuleAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): UniquePossiblyReleasedModuleAccess = 
		_UniquePossiblyReleasedModuleAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _UniquePossiblyReleasedModuleAccess(override val accessCondition: Option[Condition]) 
		extends UniquePossiblyReleasedModuleAccess
}

/**
  * A common trait for access points that return distinct possibly released modules
  * @author Mikko Hilpinen
  * @since 24.08.2024, v1.2
  */
trait UniquePossiblyReleasedModuleAccess 
	extends UniqueVersionedModuleAccessLike[PossiblyReleasedModule, UniquePossiblyReleasedModuleAccess] 
		with SingleChronoRowModelAccess[PossiblyReleasedModule, UniquePossiblyReleasedModuleAccess] 
		with NullDeprecatableView[UniquePossiblyReleasedModuleAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * Id of the released module. 
	  * None if no module release (or value) was found.
	  */
	def releaseModuleId(implicit connection: Connection) = pullColumn(releaseModel.moduleId.column).int
	
	/**
	  * Released version. 
	  * None if no module release (or value) was found.
	  */
	def releaseVersion(implicit connection: Connection) = 
		Some(Version(pullColumn(releaseModel.version.column).getString))
	
	/**
	  * Name of the generated jar file. 
	  * None if no module release (or value) was found.
	  */
	def releaseJarName(implicit connection: Connection) = pullColumn(releaseModel.jarName.column).getString
	
	/**
	  * Id of the documentation of this release. None if there is no documentation specific to this version. 
	  * None if no module release (or value) was found.
	  */
	def releaseDocId(implicit connection: Connection) = pullColumn(releaseModel.docId.column).int
	
	/**
	  * A database model (factory) used for interacting with the linked release
	  */
	protected def releaseModel = ModuleReleaseDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = PossiblyReleasedModuleDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): UniquePossiblyReleasedModuleAccess = 
		UniquePossiblyReleasedModuleAccess(condition)
	
	
	// OTHER	--------------------
	
	/**
	  * Updates the doc ids of the targeted module releases
	  * @param newDocId A new doc id to assign
	  * @return Whether any module release was affected
	  */
	def releaseDocId_=(newDocId: Int)(implicit connection: Connection) = 
		putColumn(releaseModel.docId.column, newDocId)
}

