package vf.optidepy.database.access.many.library.doc.link

import utopia.flow.collection.immutable.IntSet
import utopia.flow.generic.casting.ValueConversions._
import utopia.vault.database.Connection
import utopia.vault.nosql.access.many.model.ManyModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.FilterableView
import vf.optidepy.database.props.library.PlacedLinkDbProps

/**
  * A common trait for access points which target multiple placed links or similar instances at a time
  * @tparam A Type of read (placed links -like) instances
  * @tparam Repr Type of this access point
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait ManyPlacedLinksAccessLike[+A, +Repr] extends ManyModelAccess[A] with Indexed with FilterableView[Repr]
{
	// ABSTRACT	--------------------
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	protected def model: PlacedLinkDbProps
	
	
	// COMPUTED	--------------------
	
	/**
	  * parent ids of the accessible placed links
	  */
	def parentIds(implicit connection: Connection) = pullColumn(model.parentId.column).map { v => v.getInt }
	
	/**
	  * child ids of the accessible placed links
	  */
	def childIds(implicit connection: Connection) = pullColumn(model.childId.column).map { v => v.getInt }
	
	/**
	  * order indices of the accessible placed links
	  */
	def orderIndices(implicit connection: Connection) = pullColumn(model.orderIndex.column)
		.map { v => v.getInt }
	
	/**
	  * Unique ids of the accessible placed links
	  */
	def ids(implicit connection: Connection) = pullColumn(index).map { v => v.getInt }
	
	
	// OTHER	--------------------
	
	/**
	  * @param orderIndex order index to target
	  * @return Copy of this access point that only includes placed links with the specified order index
	  */
	def atIndex(orderIndex: Int) = filter(model.orderIndex.column <=> orderIndex)
	
	/**
	  * @param orderIndices Targeted order indices
	  * @return Copy of this access point that only includes placed links where order index is within the
	  *  specified value set
	  */
	def atIndices(orderIndices: Iterable[Int]) = filter(model.orderIndex.column.in(IntSet.from(orderIndices)))
	
	/**
	  * @param childId child id to target
	  * @return Copy of this access point that only includes placed links with the specified child id
	  */
	def toChild(childId: Int) = filter(model.childId.column <=> childId)
	
	/**
	  * @param childIds Targeted child ids
	  * @return Copy of this access point that only includes placed links where child id is within the
	  *  specified value set
	  */
	def toChildren(childIds: Iterable[Int]) = filter(model.childId.column.in(IntSet.from(childIds)))
	
	/**
	  * @param parentId parent id to target
	  * @return Copy of this access point that only includes placed links with the specified parent id
	  */
	def underParent(parentId: Int) = filter(model.parentId.column <=> parentId)
	
	/**
	  * @param parentIds Targeted parent ids
	  * @return Copy of this access point that only includes placed links where parent id is within the
	  *  specified value set
	  */
	def underParents(parentIds: Iterable[Int]) = filter(model.parentId.column.in(IntSet.from(parentIds)))
}

