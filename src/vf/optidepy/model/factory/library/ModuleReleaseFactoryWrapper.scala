package vf.optidepy.model.factory.library

import utopia.flow.util.{Mutate, Version}

/**
  * Common trait for classes that implement ModuleReleaseFactory by wrapping a ModuleReleaseFactory instance
  * @tparam A Type of constructed instances
  * @tparam Repr Implementing type of this factory
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait ModuleReleaseFactoryWrapper[A <: ModuleReleaseFactory[A], +Repr] extends ModuleReleaseFactory[Repr]
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
	
	override def withDocId(docId: Int) = mapWrapped { _.withDocId(docId) }
	
	override def withJarName(jarName: String) = mapWrapped { _.withJarName(jarName) }
	
	override def withModuleId(moduleId: Int) = mapWrapped { _.withModuleId(moduleId) }
	
	override def withVersion(version: Version) = mapWrapped { _.withVersion(version) }
	
	
	// OTHER	--------------------
	
	/**
	  * Modifies this item by mutating the wrapped factory instance
	  * @param f A function for mutating the wrapped factory instance
	  * @return Copy of this item with a mutated wrapped factory
	  */
	protected def mapWrapped(f: Mutate[A]) = wrap(f(wrappedFactory))
}

