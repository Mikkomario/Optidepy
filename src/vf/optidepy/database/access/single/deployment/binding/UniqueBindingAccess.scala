package vf.optidepy.database.access.single.deployment.binding

import utopia.flow.generic.model.immutable.Value
import utopia.flow.parse.file.FileExtensions._
import utopia.vault.database.Connection
import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.access.template.model.DistinctModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.{FilterableView, ViewFactory}
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.deployment.BindingDbFactory
import vf.optidepy.database.storable.deployment.BindingDbModel
import vf.optidepy.model.stored.deployment.Binding

import java.nio.file.Path

object UniqueBindingAccess extends ViewFactory[UniqueBindingAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): UniqueBindingAccess = _UniqueBindingAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _UniqueBindingAccess(override val accessCondition: Option[Condition]) 
		extends UniqueBindingAccess
}

/**
  * A common trait for access points that return individual and distinct bindings.
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait UniqueBindingAccess 
	extends SingleRowModelAccess[Binding] with DistinctModelAccess[Binding, Option[Binding], Value] 
		with FilterableView[UniqueBindingAccess] with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * Id of the configuration this binding belongs to. 
	  * None if no binding (or value) was found.
	  */
	def configId(implicit connection: Connection) = pullColumn(model.configId.column).int
	
	/**
	  * Path to the file or directory that is being deployed. Relative to the input root directory. 
	  * None if no binding (or value) was found.
	  */
	def source(implicit connection: Connection) = Some(pullColumn(model.source.column).getString: Path)
	
	/**
	  * Path to the directory or file where the 'source' is copied. Relative to the root output directory. 
	  * None if no binding (or value) was found.
	  */
	def target(implicit connection: Connection) = Some(pullColumn(model.target.column).getString: Path)
	
	/**
	  * Unique id of the accessible binding. None if no binding was accessible.
	  */
	def id(implicit connection: Connection) = pullColumn(index).int
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	protected def model = BindingDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = BindingDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): UniqueBindingAccess = UniqueBindingAccess(condition)
}

