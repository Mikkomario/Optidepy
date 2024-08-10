package vf.optidepy.model.partial.library

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.model.immutable.Model
import utopia.flow.generic.model.template.ModelConvertible

/**
  * Common trait for classes which provide access to placed link properties
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait HasPlacedLinkProps extends ModelConvertible
{
	// ABSTRACT	--------------------
	
	/**
	  * Id of the element within which the linked item is placed
	  */
	def parentId: Int
	
	/**
	  * Id of the linked / placed element
	  */
	def childId: Int
	
	/**
	  * 0-based index determining the location where the linked item is placed
	  */
	def orderIndex: Int
	
	
	// IMPLEMENTED	--------------------
	
	override def toModel = Model(Vector("parentId" -> parentId, "childId" -> childId, 
		"orderIndex" -> orderIndex))
}

