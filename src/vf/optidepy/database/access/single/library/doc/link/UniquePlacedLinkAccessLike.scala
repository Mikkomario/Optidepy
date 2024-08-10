package vf.optidepy.database.access.single.library.doc.link

import utopia.flow.generic.model.immutable.Value
import utopia.vault.database.Connection
import utopia.vault.nosql.access.single.model.SingleModelAccess
import utopia.vault.nosql.access.template.model.DistinctModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.FilterableView
import vf.optidepy.database.props.library.PlacedLinkDbProps

/**
  * A common trait for access points which target individual placed links or similar items at a time
  * @tparam A Type of read (placed links -like) instances
  * @tparam Repr Type of this access point
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait UniquePlacedLinkAccessLike[+A, +Repr] 
	extends SingleModelAccess[A] with DistinctModelAccess[A, Option[A], Value] with FilterableView[Repr] 
		with Indexed
{
	// ABSTRACT	--------------------
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	protected def model: PlacedLinkDbProps
	
	
	// COMPUTED	--------------------
	
	/**
	  * Id of the element within which the linked item is placed. 
	  * None if no placed link (or value) was found.
	  */
	def parentId(implicit connection: Connection) = pullColumn(model.parentId.column).int
	
	/**
	  * Id of the linked / placed element. 
	  * None if no placed link (or value) was found.
	  */
	def childId(implicit connection: Connection) = pullColumn(model.childId.column).int
	
	/**
	  * 0-based index determining the location where the linked item is placed. 
	  * None if no placed link (or value) was found.
	  */
	def orderIndex(implicit connection: Connection) = pullColumn(model.orderIndex.column).int
	
	/**
	  * Unique id of the accessible placed link. None if no placed link was accessible.
	  */
	def id(implicit connection: Connection) = pullColumn(index).int
}

