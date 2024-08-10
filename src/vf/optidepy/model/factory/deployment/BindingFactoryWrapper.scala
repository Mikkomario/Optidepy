package vf.optidepy.model.factory.deployment

import utopia.flow.util.Mutate

import java.nio.file.Path

/**
  * Common trait for classes that implement BindingFactory by wrapping a BindingFactory instance
  * @tparam A Type of constructed instances
  * @tparam Repr Implementing type of this factory
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait BindingFactoryWrapper[A <: BindingFactory[A], +Repr] extends BindingFactory[Repr]
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
	
	override def withConfigId(configId: Int) = mapWrapped { _.withConfigId(configId) }
	
	override def withSource(source: Path) = mapWrapped { _.withSource(source) }
	
	override def withTarget(target: Path) = mapWrapped { _.withTarget(target) }
	
	
	// OTHER	--------------------
	
	/**
	  * Modifies this item by mutating the wrapped factory instance
	  * @param f A function for mutating the wrapped factory instance
	  * @return Copy of this item with a mutated wrapped factory
	  */
	protected def mapWrapped(f: Mutate[A]) = wrap(f(wrappedFactory))
}

