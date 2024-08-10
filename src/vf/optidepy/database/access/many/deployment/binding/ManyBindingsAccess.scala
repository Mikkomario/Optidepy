package vf.optidepy.database.access.many.deployment.binding

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.parse.file.FileExtensions._
import utopia.vault.database.Connection
import utopia.vault.nosql.access.many.model.ManyRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.{FilterableView, ViewFactory}
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.deployment.BindingDbFactory
import vf.optidepy.database.storable.deployment.BindingDbModel
import vf.optidepy.model.stored.deployment.Binding

import java.nio.file.Path

object ManyBindingsAccess extends ViewFactory[ManyBindingsAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): ManyBindingsAccess = _ManyBindingsAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _ManyBindingsAccess(override val accessCondition: Option[Condition]) 
		extends ManyBindingsAccess
}

/**
  * A common trait for access points which target multiple bindings at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait ManyBindingsAccess 
	extends ManyRowModelAccess[Binding] with FilterableView[ManyBindingsAccess] with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * config ids of the accessible bindings
	  */
	def configIds(implicit connection: Connection) = pullColumn(model.configId.column).map { v => v.getInt }
	
	/**
	  * sources of the accessible bindings
	  */
	def sources(implicit connection: Connection) = 
		pullColumn(model.source.column).flatMap { _.string }.map { v => v: Path }
	
	/**
	  * targets of the accessible bindings
	  */
	def targets(implicit connection: Connection) = 
		pullColumn(model.target.column).flatMap { _.string }.map { v => v: Path }
	
	/**
	  * Unique ids of the accessible bindings
	  */
	def ids(implicit connection: Connection) = pullColumn(index).map { v => v.getInt }
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	protected def model = BindingDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = BindingDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): ManyBindingsAccess = ManyBindingsAccess(condition)
	
	
	// OTHER	--------------------
	
	/**
	  * @param configId config id to target
	  * @return Copy of this access point that only includes bindings with the specified config id
	  */
	def inConfig(configId: Int) = filter(model.configId.column <=> configId)
	
	/**
	  * @param configIds Targeted config ids
	  * @return Copy of this access point that only includes bindings where config id is within the specified
	  *  value set
	  */
	def inConfigs(configIds: Iterable[Int]) = filter(model.configId.column.in(configIds))
}

