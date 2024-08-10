package vf.optidepy.model.factory.deployment

import utopia.flow.util.{Mutate, Version}

import java.time.Instant

/**
  * Common trait for classes that implement DeploymentFactory by wrapping a DeploymentFactory instance
  * @tparam A Type of constructed instances
  * @tparam Repr Implementing type of this factory
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait DeploymentFactoryWrapper[A <: DeploymentFactory[A], +Repr] extends DeploymentFactory[Repr]
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
	
	override def withBranchId(branchId: Int) = mapWrapped { _.withBranchId(branchId) }
	
	override def withCreated(created: Instant) = mapWrapped { _.withCreated(created) }
	
	override def withIndex(index: Int) = mapWrapped { _.withIndex(index) }
	
	override def withVersion(version: Version) = mapWrapped { _.withVersion(version) }
	
	
	// OTHER	--------------------
	
	/**
	  * Modifies this item by mutating the wrapped factory instance
	  * @param f A function for mutating the wrapped factory instance
	  * @return Copy of this item with a mutated wrapped factory
	  */
	protected def mapWrapped(f: Mutate[A]) = wrap(f(wrappedFactory))
}

