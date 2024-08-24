package vf.optidepy.database.access.many.deployment.config

import utopia.flow.parse.file.FileExtensions._
import utopia.vault.database.Connection
import utopia.vault.nosql.view.ViewFactory
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.deployment.DeploymentConfigWithBindingsDbFactory
import vf.optidepy.database.storable.deployment.BindingDbModel
import vf.optidepy.model.combined.deployment.DeploymentConfigWithBindings

import java.nio.file.Path

object ManyDeploymentConfigsWithBindingsAccess extends ViewFactory[ManyDeploymentConfigsWithBindingsAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): ManyDeploymentConfigsWithBindingsAccess =
		_ManyDeploymentConfigsWithBindingsAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _ManyDeploymentConfigsWithBindingsAccess(override val accessCondition: Option[Condition])
		extends ManyDeploymentConfigsWithBindingsAccess
}

/**
  * A common trait for access points that return multiple detailed deployment configs at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024
  */
trait ManyDeploymentConfigsWithBindingsAccess
	extends ManyDeploymentConfigsAccessLike[DeploymentConfigWithBindings, ManyDeploymentConfigsWithBindingsAccess]
{
	// COMPUTED	--------------------
	
	/**
	  * config ids of the accessible bindings
	  */
	def bindingConfigIds(implicit connection: Connection) = 
		pullColumn(bindingModel.configId.column).map { v => v.getInt }
	
	/**
	  * sources of the accessible bindings
	  */
	def bindingSources(implicit connection: Connection) = 
		pullColumn(bindingModel.source.column).flatMap { _.string }.map { v => v: Path }
	
	/**
	  * targets of the accessible bindings
	  */
	def bindingTargets(implicit connection: Connection) = 
		pullColumn(bindingModel.target.column).flatMap { _.string }.map { v => v: Path }
	
	/**
	  * Model (factory) used for interacting the bindings associated with this detailed deployment config
	  */
	protected def bindingModel = BindingDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = DeploymentConfigWithBindingsDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): ManyDeploymentConfigsWithBindingsAccess =
		ManyDeploymentConfigsWithBindingsAccess(condition)
}

