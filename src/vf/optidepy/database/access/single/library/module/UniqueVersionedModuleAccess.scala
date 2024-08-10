package vf.optidepy.database.access.single.library.module

import utopia.vault.nosql.access.single.model.SingleChronoRowModelAccess
import utopia.vault.nosql.view.{NullDeprecatableView, ViewFactory}
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.VersionedModuleDbFactory
import vf.optidepy.model.stored.library.VersionedModule

object UniqueVersionedModuleAccess extends ViewFactory[UniqueVersionedModuleAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): UniqueVersionedModuleAccess = 
		_UniqueVersionedModuleAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _UniqueVersionedModuleAccess(override val accessCondition: Option[Condition]) 
		extends UniqueVersionedModuleAccess
}

/**
  * A common trait for access points that return individual and distinct versioned modules.
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait UniqueVersionedModuleAccess 
	extends UniqueVersionedModuleAccessLike[VersionedModule, UniqueVersionedModuleAccess] 
		with SingleChronoRowModelAccess[VersionedModule, UniqueVersionedModuleAccess] 
		with NullDeprecatableView[UniqueVersionedModuleAccess]
{
	// IMPLEMENTED	--------------------
	
	override def factory = VersionedModuleDbFactory
	
	override protected def self = this
	
	override
		 def apply(condition: Condition): UniqueVersionedModuleAccess = UniqueVersionedModuleAccess(condition)
}

