package vf.optidepy.model.factory.library

import utopia.flow.util.Mutate

/**
  * Common trait for classes that implement PlacedLinkFactory by wrapping a PlacedLinkFactory instance
  * @tparam A Type of constructed instances
  * @tparam Repr Implementing type of this factory
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait PlacedLinkFactoryWrapper[A <: PlacedLinkFactory[A], +Repr] extends PlacedLinkFactory[Repr]
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
	
	override def withChildId(childId: Int) = mapWrapped { _.withChildId(childId) }
	override def withOrderIndex(orderIndex: Int) = mapWrapped { _.withOrderIndex(orderIndex) }
	override def withParentId(parentId: Int) = mapWrapped { _.withParentId(parentId) }
	
	
	// OTHER	--------------------
	
	/**
	  * Modifies this item by mutating the wrapped factory instance
	  * @param f A function for mutating the wrapped factory instance
	  * @return Copy of this item with a mutated wrapped factory
	  */
	protected def mapWrapped(f: Mutate[A]) = wrap(f(wrappedFactory))
}

