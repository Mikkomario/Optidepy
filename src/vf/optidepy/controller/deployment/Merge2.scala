package vf.optidepy.controller.deployment

import utopia.flow.collection.CollectionExtensions._
import utopia.flow.parse.file.FileExtensions._
import utopia.flow.time.TimeExtensions._
import utopia.flow.time.Today
import utopia.flow.util.EitherExtensions._
import utopia.flow.util.TryExtensions._
import vf.optidepy.model.combined.deployment.BranchWithDeployments
import vf.optidepy.model.partial.deployment.DeploymentConfigData

import java.nio.file.Path
import java.time.Instant
import scala.util.Success

/**
 * Merges recent project builds into a single build
 * @author Mikko Hilpinen
 * @since 21.3.2023, v1.0.1
 */
object Merge2
{
	/**
	 * Merges all recent deployments of a project
	 * @param config A project
	 * @param since Time threshold for merged deployments
	 * @return Success or failure.
	 *         Contains None if no merging was done.
	 *         Contains Some(merged directory) when merging was done
	 */
	def apply(config: DeploymentConfigData, branch: BranchWithDeployments, since: Option[Instant] = None) = {
		// Finds the targeted mergeable deployments
		val targets = branch.deployments.reverseIterator
			.takeWhile { dep => since.forall { dep.created >= _ } }
			.map { dep => dep -> config.directoryForDeployment(branch.name, dep) }
			// The deployment directories must still exist
			.takeWhile { _._2.exists }
			.toOptimizedSeq
		// Case: No targets to merge
		if (targets.isEmpty)
			Success(None)
		else
			(targets.only match {
				// Case: Only one target => Simply renames the directory
				case Some((dep, dir)) => dir.rename(s"${branch.name}-merge-${ dep.deploymentIndex }-$Today")
				// Case: Multiple targets => Merges
				case None =>
					// Creates the target directory
					(config.outputDirectory/s"${branch.name}-merge-${ targets.last._1.deploymentIndex }-to-${
						targets.head._1.deploymentIndex }-$Today")
						.asExistingDirectory
						.flatMap { targetDirectory =>
							// Moves the latest copy of each file to the merge directory
							var movedPaths = Set[Path]()
							targets.tryForeach { case (_, dir) =>
								dir.allChildrenIterator
									.filter { _.toOption.forall { _.isRegularFile } }
									.map {
										_.flatMap { path =>
											val relative = path.relativeTo(dir).either
											if (!movedPaths.contains(relative)) {
												movedPaths += relative
												path.moveAs(targetDirectory/relative)
											}
											else
												Success(())
										}
									}.toTry
							// Deletes the original build directories and returns the merge directory
							}.flatMap { _ => targets.tryForeach { _._2.delete() }.map { _ => targetDirectory } }
						}
			}).map { Some(_) }
	}
}
