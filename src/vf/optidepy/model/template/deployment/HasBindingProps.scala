package vf.optidepy.model.template.deployment

import java.nio.file.Path

/**
 * Common trait for instances which name a source and a target path
 * @author Mikko Hilpinen
 * @since 10.08.2024, v1.2
 */
trait HasBindingProps
{
	/**
	 * @return Path to the file or directory that is being deployed.
	 */
	def source: Path
	/**
	 * @return Path to the directory or file where the 'source' is copied.
	 */
	def target: Path
}
