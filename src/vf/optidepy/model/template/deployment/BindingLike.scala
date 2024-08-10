package vf.optidepy.model.template.deployment

import utopia.flow.parse.file.FileExtensions._
import utopia.flow.util.Mutate

import java.nio.file.Path

/**
 * Common trait for implementations which tie a source path to a target path.
 *
 * @author Mikko Hilpinen
 * @since 10.08.2024, v1.2
 */
trait BindingLike[+Repr] extends HasBindingProps with FromBindingPathsFactory[Repr]
{
	// IMPLEMENTED  -----------------
	
	/**
	 * @param source New source to assign
	 * @return Copy of this item with the specified source
	 */
	override def withSource(source: Path): Repr = withPaths(source, target)
	/**
	 * @param target New target to assign
	 * @return Copy of this item with the specified target
	 */
	override def withTarget(target: Path): Repr = withPaths(source, target)
	
	
	// OTHER    ---------------------
	
	/**
	 * @param f Mapping function to apply to the source path
	 * @return Copy of this binding with mapped source
	 */
	def mapSource(f: Mutate[Path]) = withSource(f(source))
	/**
	 * @param f Mapping function to apply to the target path
	 * @return Copy of this binding with mapped target
	 */
	def mapTarget(f: Mutate[Path]) = withTarget(f(target))
	
	/**
	 * @param f Mapping function to apply to the source and target path
	 * @return Copy of this binding with mapped paths
	 */
	def mapPaths(f: Mutate[Path]) = withPaths(f(source), f(target))
	
	/**
	 * @param sourceDir A new parent source directory
	 * @return A copy of this binding where the current source is placed under the specified source directory
	 */
	def underSource(sourceDir: Path) = mapSource { sourceDir/_ }
	/**
	 * @param targetDir A new parent target directory
	 * @return A copy of this binding where the current target is placed under the specified target directory
	 */
	def underTarget(targetDir: Path) = mapTarget { targetDir/_ }
	
	/**
	 * @param other Another (relative) binding
	 * @return The other binding as an absolute binding (i.e. no longer relative to this one)
	 */
	def /(other: HasBindingProps) = withPaths(source/other.source, target/other.target)
	/**
	 * @param path A relative path
	 * @return A copy of this binding that targets the specified sub-path
	 */
	def /(path: Path) = mapPaths { _/path }
}
