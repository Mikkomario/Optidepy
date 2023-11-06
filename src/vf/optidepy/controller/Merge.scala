package vf.optidepy.controller

import utopia.flow.collection.CollectionExtensions._
import utopia.flow.parse.file.FileExtensions._
import utopia.flow.time.TimeExtensions._
import utopia.flow.time.Today
import vf.optidepy.model.DeployedProject

import java.nio.file.Path
import java.time.Instant
import scala.util.Success

/**
 * Merges recent project builds into a single build
 * @author Mikko Hilpinen
 * @since 21.3.2023, v1.0.1
 */
object Merge
{
	/**
	 * Merges all recent deployments of a project
	 * @param project A project
	 * @param since Time threshold for merged deployments
	 * @return Success or failure.
	 *         Contains None if no merging was done.
	 *         Contains Some(merged directory) when merging was done
	 */
	def apply(project: DeployedProject, branch: String = DeployedProject.defaultBranchName,
	          since: Option[Instant] = None) =
	{
		// Finds the targeted mergeable deployments
		val targets = project.deployments.getOrElse(branch, Vector()).reverseIterator
			.takeWhile { dep => since.forall { dep.timestamp >= _ } }
			.map { dep => dep -> project.directoryForDeployment(branch, dep) }
			// The deployment directories must still exist
			.takeWhile { _._2.exists }.toVector
		// Case: No targets to merge
		if (targets.isEmpty)
			Success(None)
		else
			(targets.only match {
				// Case: Only one target => Simply renames the directory
				case Some((dep, dir)) => dir.rename(s"$branch-merge-${ dep.index }-$Today")
				// Case: Multiple targets => Merges
				case None =>
					// Creates the target directory
					(project.output/s"$branch-merge-${ targets.last._1.index }-to-${ targets.head._1.index }-$Today")
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
