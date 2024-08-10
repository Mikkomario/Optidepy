package vf.optidepy.database.props.library
import utopia.vault.model.immutable.DbPropertyDeclaration

/**
  * Common trait for interfaces that provide access to placed link database properties by
  *  wrapping a PlacedLinkDbProps
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait PlacedLinkDbPropsWrapper extends PlacedLinkDbProps
{
	// ABSTRACT	--------------------
	
	/**
	  * The wrapped placed link database properties
	  */
	protected def placedLinkDbProps: PlacedLinkDbProps
	
	
	// IMPLEMENTED	--------------------
	
	override def id: DbPropertyDeclaration = placedLinkDbProps.id
	override def childId = placedLinkDbProps.childId
	override def orderIndex = placedLinkDbProps.orderIndex
	override def parentId = placedLinkDbProps.parentId
}

