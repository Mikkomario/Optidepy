package vf.optidepy.database.access.many.deployment.config

import utopia.flow.parse.file.FileExtensions._
import utopia.vault.database.Connection
import utopia.vault.nosql.view.ViewFactory
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.deployment.DetailedDeploymentConfigDbFactory
import vf.optidepy.database.storable.deployment.BindingDbModel
import vf.optidepy.model.combined.deployment.DetailedDeploymentConfig

import java.nio.file.Path

object ManyDetailedDeploymentConfigsAccess extends ViewFactory[ManyDetailedDeploymentConfigsAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): ManyDetailedDeploymentConfigsAccess = 
		_ManyDetailedDeploymentConfigsAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _ManyDetailedDeploymentConfigsAccess(override val accessCondition: Option[Condition]) 
		extends ManyDetailedDeploymentConfigsAccess
}

/**
  * A common trait for access points that return multiple detailed deployment configs at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024
  */
trait ManyDetailedDeploymentConfigsAccess 
	extends ManyDeploymentConfigsAccessLike[DetailedDeploymentConfig, ManyDetailedDeploymentConfigsAccess]
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
	
	override def factory = DetailedDeploymentConfigDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): ManyDetailedDeploymentConfigsAccess = 
		ManyDetailedDeploymentConfigsAccess(condition)
}

