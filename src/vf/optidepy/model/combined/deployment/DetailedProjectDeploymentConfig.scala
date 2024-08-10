package vf.optidepy.model.combined.deployment

import utopia.flow.parse.file.FileExtensions._
import utopia.flow.view.template.Extender
import utopia.vault.model.template.HasId
import vf.optidepy.model.factory.deployment.DeploymentConfigFactoryWrapper
import vf.optidepy.model.partial.deployment.DeploymentConfigData
import vf.optidepy.model.stored.deployment.{Binding, DeploymentConfig}
import vf.optidepy.model.stored.project.Project

/**
 * Contains deployment configuration information, standard project information,
 * as well as individual file / directory bindings.
 * @param config Wrapped deployment config
 * @param project Project to deploy
 * @param bindings Bindings that determine, which files are moved and where. Relative.
 * @author Mikko Hilpinen
 * @since 10.08.2024, v1.2
 */
case class DetailedProjectDeploymentConfig(config: DeploymentConfig, project: Project, bindings: Seq[Binding])
	extends ProjectDeploymentConfig
{
	// ATTRIBUTES   ------------------------
	
	/**
	 * Directory that hosts all the input files. All input bindings are relative to this path by default.
	 */
	lazy val inputDirectory = config.relativeInputDirectory match {
		case Some(input) => project.rootPath/input
		case None => project.rootPath
	}
	/**
	 * @return Directory bindings where input is absolute and output is relative
	 */
	lazy val sourceCorrectedBindings = bindings.map { _.underSource(inputDirectory) }
	
	
	// COMPUTED ----------------------------
	
	
	// IMPLEMENTED  ------------------------
	
	override protected def wrap(factory: DeploymentConfig): DetailedProjectDeploymentConfig = copy(config = factory)
}