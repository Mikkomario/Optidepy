package vf.optidepy.database.access.many.library.module

import utopia.vault.nosql.access.many.model.ManyRowModelAccess
import utopia.vault.nosql.view.{ChronoRowFactoryView, ViewFactory}
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.VersionedModuleDbFactory
import vf.optidepy.model.stored.library.VersionedModule

object ManyVersionedModulesAccess extends ViewFactory[ManyVersionedModulesAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): ManyVersionedModulesAccess = 
		_ManyVersionedModulesAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _ManyVersionedModulesAccess(override val accessCondition: Option[Condition]) 
		extends ManyVersionedModulesAccess
}

/**
  * A common trait for access points which target multiple versioned modules at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait ManyVersionedModulesAccess 
	extends ManyVersionedModulesAccessLike[VersionedModule, ManyVersionedModulesAccess] 
		with ManyRowModelAccess[VersionedModule] 
		with ChronoRowFactoryView[VersionedModule, ManyVersionedModulesAccess]
{
	// IMPLEMENTED	--------------------
	
	override def factory = VersionedModuleDbFactory
	
	override protected def self = this
	
	override
		 def apply(condition: Condition): ManyVersionedModulesAccess = ManyVersionedModulesAccess(condition)
}

