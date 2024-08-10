package vf.optidepy.database.access.single.library.doc.section

import utopia.flow.generic.model.immutable.Value
import utopia.vault.database.Connection
import utopia.vault.nosql.access.single.model.SingleModelAccess
import utopia.vault.nosql.access.template.model.DistinctModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.FilterableView
import vf.optidepy.database.storable.library.DocSectionDbModel

/**
  * A common trait for access points which target individual doc sections or similar items at a time
  * @tparam A Type of read (doc sections -like) instances
  * @tparam Repr Type of this access point
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait UniqueDocSectionAccessLike[+A, +Repr] 
	extends SingleModelAccess[A] with DistinctModelAccess[A, Option[A], Value] with FilterableView[Repr] 
		with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * Id of the header (text) of this section. 
	  * None if no doc section (or value) was found.
	  */
	def headerId(implicit connection: Connection) = pullColumn(model.headerId.column).int
	
	/**
	  * Time when this section (version) was added. 
	  * None if no doc section (or value) was found.
	  */
	def created(implicit connection: Connection) = pullColumn(model.created.column).instant
	
	/**
	  * Unique id of the accessible doc section. None if no doc section was accessible.
	  */
	def id(implicit connection: Connection) = pullColumn(index).int
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	protected def model = DocSectionDbModel
}

