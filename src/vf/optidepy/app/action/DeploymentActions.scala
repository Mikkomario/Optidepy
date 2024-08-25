package vf.optidepy.app.action

import utopia.flow.collection.CollectionExtensions._
import utopia.flow.collection.immutable.{Empty, Single}
import utopia.flow.parse.file.FileExtensions._
import utopia.flow.operator.equality.EqualsExtensions._
import utopia.flow.operator.equality.EqualsFunction
import utopia.flow.util.StringExtensions._
import utopia.flow.util.console.ConsoleExtensions._
import utopia.flow.util.logging.Logger
import utopia.vault.database.Connection
import vf.optidepy.controller.IndexCounter
import vf.optidepy.controller.deployment.Deploy2
import vf.optidepy.database.access.many.deployment.DbDeployments
import vf.optidepy.database.access.many.deployment.binding.DbBindings
import vf.optidepy.database.access.many.deployment.branch.DbBranches
import vf.optidepy.database.access.many.deployment.config.{DbDeploymentConfigs, DbProjectDeploymentConfigs}
import vf.optidepy.database.access.many.project.DbProjects
import vf.optidepy.database.storable.deployment.{BranchDbModel, DeploymentDbModel}
import vf.optidepy.model.combined.deployment.{PossiblyDeployedBranch, ProjectBranch, ProjectDeploymentConfig, ProjectDeploymentConfigWithBindings}
import vf.optidepy.model.partial.deployment.BranchData

import java.time.Instant
import scala.concurrent.ExecutionContext
import scala.io.StdIn
import scala.util.{Failure, Success}

/**
 * An interface that provides interactive deployment-related functions
 *
 * @author Mikko Hilpinen
 * @since 23.08.2024, v1.2
 */
object DeploymentActions
{
	/**
	 * Deploys a project or part of a project
	 * @param target Targeted project and/or configuration name. If empty, user will be prompted to select the target.
	 * @param branchName Name of the targeted branch.
	 *                   If empty, default branch will be targeted,
	 *                   or, if that is not possible, the user will be prompted to select a branch.
	 * @param since Time after which all changes should be included, regardless of last deployment time.
	 *              None if only changes since the latest deployment should be included (default).
	 *              NB: Setting 'fullRebuild' to true overrides this value.
	 * @param fullRebuild Whether all changes from all times should be included.
	 *                    Default = false = only changes since the last deployment
	 *                                      or the targeted 'since' time will be included.
	 * @param connection Implicit DB connection
	 * @param exc Implicit execution context
	 * @param log Implicit logging implementation
	 */
	def deploy(target: String = "", branchName: String = "", since: Option[Instant] = None,
	           fullRebuild: Boolean = false)
	          (implicit connection: Connection, exc: ExecutionContext, log: Logger) =
	{
		// Determines the targeted project & deployment configuration
		val targetedConfigs = if (target.isEmpty) selectConfig() else identifyConfig(target)
		if (targetedConfigs.isEmpty)
			println("No deployment target was selected => Deployment canceled.")
		else {
			// Determines the targeted branch
			val targetedBranches = {
				if (branchName.isEmpty)
					selectBranch(targetedConfigs)
				else
					identifyBranch(targetedConfigs, branchName)
			}
			if (targetedBranches.isEmpty)
				println("No branch was targeted => Deployment canceled")
			else {
				// Loads binding data
				val bindingsPerConfig = DbBindings.inConfigs(targetedBranches.map { _.config.id }).pull
					.groupBy { _.configId }.withDefaultValue(Empty)
				
				// Deploys the targeted branches
				targetedBranches.foreach { branch =>
					val lastDeployment = DbDeployments.ofBranch(branch.id).latest.pull
					_deploy(branch.config.withBindings(bindingsPerConfig(branch.deploymentConfigId)),
						branch.branch.withDeployment(lastDeployment), since, fullRebuild)
				}
			}
		}
	}
	
	private def _deploy(config: ProjectDeploymentConfigWithBindings, branch: PossiblyDeployedBranch,
	                    includeChangesAfter: Option[Instant] = None, fullRebuild: Boolean = false)
	                   (implicit connection: Connection, exc: ExecutionContext, log: Logger): Unit =
	{
		// Determines the next deployment index
		implicit val counter: IndexCounter = new IndexCounter(DbDeployments.ofBranch(branch.id).latest
			.deploymentIndex match
		{
			case Some(lastIndex) => lastIndex + 1
			case None => 1
		})
		
		// Performs the deployment
		println(s"Deploying $config${
			branch.name.mapIfNotEmpty { b => s" ($b)" } }")
		Deploy2(config, branch, includeChangesAfter, fullRebuild = fullRebuild) match {
			case Success(deployment) =>
				deployment match {
					case Some(deployment) =>
						println("Deployment complete!")
						
						// Stores the deployment into the DB
						DeploymentDbModel.insert(deployment)
						
						// May open the deployment directory afterwards
						if (config.usesBuildDirectories)
							config.directoryForDeployment(branch.name, deployment).openDirectory()
					
					case None => println("No files were deployed")
				}
			
			case Failure(error) => log(error, "Deployment failed")
		}
	}
	
	// Converts a target string into 0-n targeted deployment configurations
	// NB: Assumes non-empty targetName
	private def identifyConfig(targetName: String)(implicit connection: Connection) = {
		val (generalPart, specificPart) = targetName.splitAtFirst("/").toTuple
		DbProjectDeploymentConfigs.matching(generalPart, specificPart).pull.emptyOneOrMany match {
			case None =>
				println(s"'$targetName' didn't match any deployment configuration")
				Empty
			case Some(Left(target)) => Single(target)
			case Some(Right(options)) =>
				println(s"Found ${ options.size } deployment configurations that match '$targetName'. \nWhich of these do you want to deploy?")
				val extendedOptions = options.groupBy { _.project.id }.view
					.flatMap { case (_, configs) =>
						val defaultOptions = configs.sortBy { _.name }.map { c => Single(c) -> c.toString }
						if (configs.hasSize > 1)
							defaultOptions :+
								(configs -> s"${ configs.head.project.name } (${ configs.size } configurations)")
						else
							defaultOptions
					}
					.toOptimizedSeq
				StdIn.selectFrom(extendedOptions, "configurations", "deploy").getOrElse(Empty)
		}
	}
	
	// Allows the user to select the deployed project or configuration
	private def selectConfig()(implicit connection: Connection) = {
		// Requests the user to select the targeted project
		val projects = DbProjects.pull
		StdIn.selectFrom(projects.sortBy { _.name }.map { p => p -> p.name }, "projects", "deploy") match {
			case Some(project) =>
				// Loads deployment configurations
				val configs = DbDeploymentConfigs.ofProject(project.id).pull
				val selectedConfigs = configs.only match {
					// Case: Only one configuration in this project => Deploys that one
					case Some(onlyConfig) => Single(onlyConfig)
					// Case: Multiple configurations available => Allows the user to select one or all of them
					case None =>
						StdIn.selectFrom(
							configs.map { c => Single(c) -> c.name.nonEmptyOrElse("default") } :+
								(configs -> "all of these"),
							"configurations", "deploy")
							.getOrElse(Empty)
				}
				// Includes project data in the result
				selectedConfigs.map { ProjectDeploymentConfig(_, project) }
				
			// Case: No project selected => Cancels
			case None => Empty
		}
	}
	
	// NB: Assumes that 'configs' is not empty
	private def identifyBranch(configs: Seq[ProjectDeploymentConfig], branchName: String)
	                          (implicit connection: Connection): Seq[ProjectBranch] =
	{
		val configBranchAccess = DbBranches.ofDeploymentConfigs(configs.map { _.id })
		
		// Branches are disabled if there exist unnamed branches
		val nonBranchingConfigIds = configBranchAccess.unnamed.deploymentConfigIds.toSet
		if (nonBranchingConfigIds.nonEmpty) {
			val illegalConfigs = configs.filter { c => nonBranchingConfigIds.contains(c.id) }
			if (illegalConfigs.hasSize > 1)
				println(s"The following configurations don't support branching: [${
					illegalConfigs.mkString(", ") }] => Can't deploy '$branchName'")
			else
				println(s"${ illegalConfigs.head } doesn't support branching => Can't deploy '$branchName'")
			
			Empty
		}
		else {
			// Checks whether the targeted branch exists or not
			val existingBranches = configBranchAccess.withName(branchName).pull
			
			// Case: Targeted branch doesn't exist => Requests the user whether they want to create a new branch
			if (existingBranches.isEmpty) {
				// Case: User wants to create a new branch
				if (StdIn.ask(
					s"Branch '$branchName' doesn't exist yet. Do you want to create a new branch with this name?"))
				{
					val makeDefault = configBranchAccess.defaults.isEmpty &&
						StdIn.ask("Do you want to make this the default branch?")
					insertBranchesFor(configs, branchName, makeDefault)
				}
				// Case: Canceled by the user
				else
					Empty
			}
			// Case: Targeted branch exists in at least some configurations => Checks whether it exists in all of them
			else {
				val (configsMissingBranch, readyBranches) = configs.divideWith { c =>
					existingBranches.find { _.deploymentConfigId == c.id } match {
						case Some(b) => Right(ProjectBranch(b, c))
						case None => Left(c)
					}
				}
				if (configsMissingBranch.isEmpty)
					readyBranches
				else if (StdIn.ask(s"The following configurations don't contain branch '$branchName' yet: [${
					configsMissingBranch.mkString(", ") }]. Do you want to add this branch to these configurations, also?",
					default = true))
				{
					val makeDefault = DbBranches.ofDeploymentConfigs(configsMissingBranch.map { _.id }).defaults.isEmpty &&
						StdIn.ask(s"Do you want to make '$branchName' the default branch for these configurations?",
							default = DbBranches(readyBranches.map { _.id }).areAllDefaults)
					val newBranches = insertBranchesFor(configsMissingBranch, branchName, makeDefault)
					
					readyBranches ++ newBranches
				}
				else if (StdIn.ask(s"Do you instead want to deploy just the following configuration(s): ${
					readyBranches.view.map { _.config }.mkString(", ") }"))
					readyBranches
				else
					Empty
			}
		}
	}
	
	// NB: Assumes that 'configs' is not empty
	private def selectBranch(configs: Seq[ProjectDeploymentConfig])(implicit connection: Connection): Seq[ProjectBranch] =
	{
		// Checks whether default branches exist for the targeted configurations
		val branchAccess = DbBranches.ofDeploymentConfigs(configs.map { _.id })
		val defaultBranches = branchAccess.defaults.pull
		
		// Case: Default branches are used at least in some configurations
		//       => Checks whether that is the case for all the configurations
		if (defaultBranches.nonEmpty) {
			val (configsMissingDefaultBranch, readyBranches) = configs.divideWith { config =>
				defaultBranches.find { _.deploymentConfigId == config.id } match {
					case Some(branch) => Right(branch.inConfig(config))
					case None => Left(config)
				}
			}
			// Case: All targeted configurations have a default branch => Uses that
			if (configsMissingDefaultBranch.isEmpty)
				readyBranches
			// Case: Some configurations don't have a default branch
			//       => May either create a default branch or make one of the existing branches the default,
			//          depending on the situation and user choice
			else if (StdIn.ask(s"The following configurations don't have a default branch: [${
				configsMissingDefaultBranch.mkString(", ") }]. Do you want to create or select a default branch for these deployment configurations?"))
			{
				// Loads existing branch data
				val existingBranchesPerConfig = DbBranches
					.ofDeploymentConfigs(configsMissingDefaultBranch.map { _.id }).pull
					.groupBy { _.deploymentConfigId }.withDefaultValue(Empty)
				
				// Allows the user to choose the action for each configuration
				// Each entry contains either:
				//      Left: Targeted configuration, plus name of a new branch
				//      Right: Existing branch that should be made the default branch
				// There might not exist an entry for each targeted configuration
				val modifiedConfigs = configsMissingDefaultBranch.flatMap { config =>
					val existingBranches = existingBranchesPerConfig(config.id)
					// Case: No existing branches => Asks the user to create a branch. Unnamed branches are supported.
					if (existingBranches.isEmpty) {
						println(s"Please specify a name for the branch to create for $config.")
						println("Empty input means that no branching should ever be used for this configuration.")
						println("Enter \"-\" to cancel branch creation for this configuration.")
						val newBranchName = StdIn.readLine()
						if (newBranchName == "-")
							None
						else
							Some(Left(config -> newBranchName))
					}
					// Case: Existing branches => Asks the user to select or to create a branch.
					//                            Unnamed branches are not supported.
					else {
						StdIn.selectFromOrAdd[Either[(ProjectDeploymentConfig, String), ProjectBranch]](
							existingBranches.map { b => Right(b.inConfig(config)) -> b.name }, "branches") {
							println(s"Please specify a name for the branch to create for $config.")
							println("Empty input cancels branch creation.")
							StdIn.readNonEmptyLine().flatMap { branchName =>
								if (branchName.isEmpty)
									None
								else
									Some(existingBranches.find { _.name ~== branchName } match {
										case Some(existingMatch) => Right(existingMatch.inConfig(config))
										case None => Left(config -> branchName)
									})
							}
						}
					}
				}
				
				// Case: Modifications were made and were accepted
				//       => Performs them and returns the prepared configurations
				if (modifiedConfigs.nonEmpty && (modifiedConfigs.size == configsMissingDefaultBranch.size ||
					StdIn.ask("No default branch was defined for some of the configurations. Do you want to continue with the other configurations anyway?",
						default = true)))
				{
					// Performs the default branch updates
					val (branchesToInsert, branchesToMakeDefault) = modifiedConfigs.divided
					val updatedBranches = {
						if (branchesToMakeDefault.isEmpty)
							Empty
						else {
							DbBranches(branchesToMakeDefault.map { _.id }).areDefaults = true
							branchesToMakeDefault.map { _.asDefault }
						}
					}
					
					// Inserts the new branches
					val insertedBranches = BranchDbModel.insertFrom(branchesToInsert) { case (config, branchName) =>
						BranchData(config.id, branchName, isDefault = true)
					} { case (branch, (config, _)) => branch.inConfig(config) }
					
					readyBranches ++ updatedBranches ++ insertedBranches
				}
				else
					Empty
			}
			// Case: Branch modification was rejected
			//       => Requests whether the user wants to deploy the other branches instead
			else if (StdIn.ask(s"Do you instead want to deploy just the following configuration(s): [${
				readyBranches.mkString(", ") }]?"))
				readyBranches
			else
				Empty
		}
		// Case: No default branches exist for the targeted configurations
		//       => Requests the user to select or add a targeted branch
		else {
			val existingBranches = branchAccess.pull
			
			// Checks whether unnamed branches may be used
			//      (which requires there to not exist any branches for these configurations yet)
			//      (Use of unnamed branches means that no branching will be used for the selected configurations)
			// Case: Branching is already in use, or the user desires to use branching
			//       => Directs the user to select a branch
			if (existingBranches.nonEmpty || StdIn.ask(
				"Do you want to use branches in these configurations? Selecting no will permanently disable branching in these configurations.",
				default = true))
			{
				val existingBranchNames = existingBranches.map { _.name }
					.distinctWith(EqualsFunction.stringCaseInsensitive).sorted
				val targetBranch = StdIn.selectFromOrAdd(existingBranchNames.map { n => (n -> false) -> n },
					"branches", "deploy") {
					println("Please specify the name of this new branch (empty input cancels)")
					StdIn.readNonEmptyLine().map { _ -> true }
				}
				
				targetBranch match {
					case Some((branchName, isNewBranch)) =>
						val existingMatchingBranchPerConfig = existingBranches.view.filter { _.name ~== branchName }
							.map { b => b.deploymentConfigId -> b }.toMap
						val (configsMissingBranch, readyBranches) = configs.divideWith { config =>
							existingMatchingBranchPerConfig.get(config.id) match {
								case Some(branch) => Right(branch.inConfig(config))
								case None => Left(config)
							}
						}
						
						// Case: Ready to continue
						if (configsMissingBranch.isEmpty)
							readyBranches
						// Case: Branch-insertion is required
						//       => Acquires consent for creating new branches, unless acquired already
						else if (isNewBranch || StdIn.ask(
							s"Is it okay to add '$branchName' as a new branch to the following configuration(s): [${
								configsMissingBranch.mkString(", ") }]", default = true))
						{
							// Checks whether the new branches should be marked as the default branches
							val alreadyExistsDefaults = existingBranches.view.filter { _.isDefault }
								.exists { b => configsMissingBranch.exists { _.id == b.deploymentConfigId } }
							val makeDefault = !alreadyExistsDefaults && StdIn.ask(
								s"Do you want to make this the default branch for these configurations?")
							
							val insertedBranches = insertBranchesFor(configsMissingBranch, branchName, makeDefault)
							
							readyBranches ++ insertedBranches
						}
						// Case: Consent not acquired => Checks for a compromise, if possible
						else if (readyBranches.nonEmpty && StdIn.ask(
							s"Do you still want to deploy the following configuration(s): [${
								readyBranches.mkString(", ") }]?"))
							readyBranches
						// Case: Deployment canceled
						else
							Empty

					// Case: Branch selection canceled
					case None => Empty
				}
			}
			// Case: User desires to disable branching in these configurations
			//       => Continues with unnamed branches (which will be created)
			else
				BranchDbModel.insertFrom(configs) { c => BranchData(c.id, isDefault = true) } { _ inConfig _ }
		}
	}
	
	private def insertBranchesFor(configs: Seq[ProjectDeploymentConfig], branchName: String,
	                              makeDefault: Boolean = false)
	                             (implicit connection: Connection) =
		BranchDbModel.insertFrom(configs) { config =>
			BranchData(config.id, branchName, isDefault = makeDefault) } { _ inConfig _ }
}
