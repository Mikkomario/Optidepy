package vf.optidepy.model.factory.deployment

import utopia.flow.util.Mutate

import java.time.Instant

/**
  * Common trait for classes that implement BranchFactory by wrapping a BranchFactory instance
  * @tparam A Type of constructed instances
  * @tparam Repr Implementing type of this factory
  * @author Mikko Hilpinen
  * @since 23.08.2024, v1.2
  */
trait BranchFactoryWrapper[A <: BranchFactory[A], +Repr] extends BranchFactory[Repr]
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
	
	override def withDeploymentConfigId(deploymentConfigId: Int) = 
		mapWrapped { _.withDeploymentConfigId(deploymentConfigId) }
	
	override def withDeprecatedAfter(deprecatedAfter: Instant) = 
		mapWrapped { _.withDeprecatedAfter(deprecatedAfter) }
	
	override def withIsDefault(isDefault: Boolean) = mapWrapped { _.withIsDefault(isDefault) }
	
	override def withName(name: String) = mapWrapped { _.withName(name) }
	
	
	// OTHER	--------------------
	
	/**
	  * Modifies this item by mutating the wrapped factory instance
	  * @param f A function for mutating the wrapped factory instance
	  * @return Copy of this item with a mutated wrapped factory
	  */
	protected def mapWrapped(f: Mutate[A]) = wrap(f(wrappedFactory))
}

