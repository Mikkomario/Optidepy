package vf.optidepy.model.template.deployment

import java.nio.file.Path

/**
 * Common trait for factory classes which can create instances from source and/or target paths
 * @tparam A Type of constructed instances
 * @author Mikko Hilpinen
 * @since 10.08.2024, v1.2
 */
trait FromBindingPathsFactory[+A]
{
	// ABSTRACT ----------------------
	
	/**
	 * @param source New source path to assign
	 * @param target New target path to assign
	 * @return An instance with these paths
	 */
	def withPaths(source: Path, target: Path): A
	
	/**
	 * @param source New source to assign
	 * @return Copy of this item with the specified source
	 */
	def withSource(source: Path): A
	/**
	 * @param target New target to assign
	 * @return Copy of this item with the specified target
	 */
	def withTarget(target: Path): A
}
