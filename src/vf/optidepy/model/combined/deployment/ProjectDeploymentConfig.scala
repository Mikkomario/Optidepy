package vf.optidepy.model.combined.deployment

import utopia.flow.parse.file.FileExtensions._
import utopia.flow.util.StringExtensions._
import vf.optidepy.model.stored.deployment.{Binding, DeploymentConfig}
import vf.optidepy.model.stored.project.Project

object ProjectDeploymentConfig
{
	// OTHER    ------------------------------
	
	/**
	 * @param config Config to wrap
	 * @param project Deployed project
	 * @return Specified configuration with the specified project included
	 */
	def apply(config: DeploymentConfig, project: Project): ProjectDeploymentConfig =
		_ProjectDeploymentConfig(config, project)
	
	
	// NESTED   ------------------------------
	
	private case class _ProjectDeploymentConfig(config: DeploymentConfig, project: Project)
		extends ProjectDeploymentConfig
	{
		override protected def wrap(factory: DeploymentConfig) = copy(config = factory)
	}
}

/**
  * Combines project and deployment configuration information
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait ProjectDeploymentConfig extends CombinedDeploymentConfig[ProjectDeploymentConfig]
{
	// ABSTRACT --------------------
	
	/**
	 * @return Wrapped deployment configuration
	 */
	def config: DeploymentConfig
	/**
	 * @return Deployed project
	 */
	def project: Project
	
	
	// COMPUTED ------------------------
	
	/**
	 * Directory that hosts all the input files. All input bindings are relative to this path by default.
	 */
	def inputDirectory = project.rootPath/config.relativeInputDirectory
	
	
	// IMPLEMENTED	--------------------
	
	override def deploymentConfig: DeploymentConfig = config
	
	override def toString = config.name.ifNotEmpty match {
		case Some(configName) => s"${ project.name }/$configName"
		case None => project.name
	}
	
	
	// OTHER    -----------------------
	
	/**
	 * @param bindings Bindings to attach to this deployment configuration
	 * @return Copy of this configuration with the specified bindings included / attached
	 */
	def withBindings(bindings: Seq[Binding]) = ProjectDeploymentConfigWithBindings(config, project, bindings)
}

