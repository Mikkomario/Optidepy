package vf.optidepy.database.access.many.library.doc.section

import utopia.flow.generic.casting.ValueConversions._
import utopia.vault.database.Connection
import utopia.vault.nosql.access.many.model.ManyModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.FilterableView
import vf.optidepy.database.storable.library.DocSectionDbModel

/**
  * A common trait for access points which target multiple doc sections or similar instances at a time
  * @tparam A Type of read (doc sections -like) instances
  * @tparam Repr Type of this access point
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait ManyDocSectionsAccessLike[+A, +Repr] extends ManyModelAccess[A] with Indexed with FilterableView[Repr]
{
	// COMPUTED	--------------------
	
	/**
	  * header ids of the accessible doc sections
	  */
	def headerIds(implicit connection: Connection) = pullColumn(model.headerId.column).map { v => v.getInt }
	
	/**
	  * creation times of the accessible doc sections
	  */
	def creationTimes(implicit connection: Connection) = 
		pullColumn(model.created.column).map { v => v.getInstant }
	
	/**
	  * Unique ids of the accessible doc sections
	  */
	def ids(implicit connection: Connection) = pullColumn(index).map { v => v.getInt }
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	protected def model = DocSectionDbModel
	
	
	// OTHER	--------------------
	
	/**
	  * @param headerId header id to target
	  * @return Copy of this access point that only includes doc sections with the specified header id
	  */
	def withHeader(headerId: Int) = filter(model.headerId.column <=> headerId)
	
	/**
	  * @param headerIds Targeted header ids
	  * @return Copy of this access point that only includes doc sections where header id is within the
	  *  specified value set
	  */
	def withHeaders(headerIds: Iterable[Int]) = filter(model.headerId.column.in(headerIds))
}

