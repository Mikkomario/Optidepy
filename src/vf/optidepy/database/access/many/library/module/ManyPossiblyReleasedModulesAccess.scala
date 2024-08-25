package vf.optidepy.database.access.many.library.module

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.util.Version
import utopia.vault.database.Connection
import utopia.vault.nosql.access.many.model.ManyRowModelAccess
import utopia.vault.nosql.view.ViewFactory
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.PossiblyReleasedModuleDbFactory
import vf.optidepy.database.storable.library.ModuleReleaseDbModel
import vf.optidepy.model.combined.library.PossiblyReleasedModule

object ManyPossiblyReleasedModulesAccess extends ViewFactory[ManyPossiblyReleasedModulesAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): ManyPossiblyReleasedModulesAccess = 
		_ManyPossiblyReleasedModulesAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _ManyPossiblyReleasedModulesAccess(override val accessCondition: Option[Condition]) 
		extends ManyPossiblyReleasedModulesAccess
}

/**
  * A common trait for access points that return multiple possibly released modules at a time
  * @author Mikko Hilpinen
  * @since 24.08.2024
  */
trait ManyPossiblyReleasedModulesAccess 
	extends ManyVersionedModulesAccessLike[PossiblyReleasedModule, ManyPossiblyReleasedModulesAccess] 
		with ManyRowModelAccess[PossiblyReleasedModule]
{
	// COMPUTED	--------------------
	
	/**
	  * module ids of the accessible module releases
	  */
	def releaseModuleIds(implicit connection: Connection) = 
		pullColumn(releaseModel.moduleId.column).map { v => v.getInt }
	
	/**
	  * versions of the accessible module releases
	  */
	def releaseVersions(implicit connection: Connection) = 
		pullColumn(releaseModel.version.column).flatMap { _.string }.map { v => Version(v) }
	
	/**
	  * jar names of the accessible module releases
	  */
	def releaseJarNames(implicit connection: Connection) = 
		pullColumn(releaseModel.jarName.column).flatMap { _.string }
	
	/**
	  * doc ids of the accessible module releases
	  */
	def releaseDocIds(implicit connection: Connection) = 
		pullColumn(releaseModel.docId.column).flatMap { v => v.int }
	
	/**
	  * Model (factory) used for interacting the module releases associated with this possibly released module
	  */
	protected def releaseModel = ModuleReleaseDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = PossiblyReleasedModuleDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): ManyPossiblyReleasedModulesAccess = 
		ManyPossiblyReleasedModulesAccess(condition)
	
	
	// OTHER	--------------------
	
	/**
	  * Updates the doc ids of the targeted module releases
	  * @param newDocId A new doc id to assign
	  * @return Whether any module release was affected
	  */
	def releaseDocIds_=(newDocId: Int)(implicit connection: Connection) = 
		putColumn(releaseModel.docId.column, newDocId)
}

