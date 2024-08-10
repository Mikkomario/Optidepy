package vf.optidepy.model.factory.library

import utopia.flow.util.Mutate

import java.nio.file.Path
import java.time.Instant

/**
  * 
	Common trait for classes that implement VersionedModuleFactory by wrapping a VersionedModuleFactory instance
  * @tparam A Type of constructed instances
  * @tparam Repr Implementing type of this factory
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait VersionedModuleFactoryWrapper[A <: VersionedModuleFactory[A], +Repr] 
	extends VersionedModuleFactory[Repr]
{
	// ABSTRACT	--------------------
	
	/**
	  * The factory wrapped by this instance
	  */
	protected def wrappedFactory: A
	
	/**
	  * Mutates this item by wrapping a mutated instance
	  * @param factory The new factory instance to wrap
	  * @return Copy of this item with the specified wrapped factory
	  */
	protected def wrap(factory: A): Repr
	
	
	// IMPLEMENTED	--------------------
	
	override def withCreated(created: Instant) = mapWrapped { _.withCreated(created) }
	
	override def withDeprecatedAfter(deprecatedAfter: Instant) = 
		mapWrapped { _.withDeprecatedAfter(deprecatedAfter) }
	
	override def withName(name: String) = mapWrapped { _.withName(name) }
	
	override def withProjectId(projectId: Int) = mapWrapped { _.withProjectId(projectId) }
	
	override def withRelativeArtifactDirectory(relativeArtifactDirectory: Path) = 
		mapWrapped { _.withRelativeArtifactDirectory(relativeArtifactDirectory) }
	
	override def withRelativeChangeListPath(relativeChangeListPath: Path) = 
		mapWrapped { _.withRelativeChangeListPath(relativeChangeListPath) }
	
	
	// OTHER	--------------------
	
	/**
	  * Modifies this item by mutating the wrapped factory instance
	  * @param f A function for mutating the wrapped factory instance
	  * @return Copy of this item with a mutated wrapped factory
	  */
	protected def mapWrapped(f: Mutate[A]) = wrap(f(wrappedFactory))
}

