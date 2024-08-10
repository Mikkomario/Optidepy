package vf.optidepy.database.access.single.library.doc.text

import utopia.flow.generic.model.immutable.Value
import utopia.vault.database.Connection
import utopia.vault.nosql.access.single.model.SingleModelAccess
import utopia.vault.nosql.access.template.model.DistinctModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.FilterableView
import vf.optidepy.database.storable.library.DocTextDbModel

/**
  * A common trait for access points which target individual doc texts or similar items at a time
  * @tparam A Type of read (doc texts -like) instances
  * @tparam Repr Type of this access point
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait UniqueDocTextAccessLike[+A, +Repr] 
	extends SingleModelAccess[A] with DistinctModelAccess[A, Option[A], Value] with FilterableView[Repr] 
		with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * Wrapped text contents. 
	  * None if no doc text (or value) was found.
	  */
	def text(implicit connection: Connection) = pullColumn(model.text.column).getString
	
	/**
	  * Level of indentation applicable to this line / text. 
	  * None if no doc text (or value) was found.
	  */
	def indentation(implicit connection: Connection) = pullColumn(model.indentation.column).int
	
	/**
	  * Time when this text was first introduced. 
	  * None if no doc text (or value) was found.
	  */
	def created(implicit connection: Connection) = pullColumn(model.created.column).instant
	
	/**
	  * Unique id of the accessible doc text. None if no doc text was accessible.
	  */
	def id(implicit connection: Connection) = pullColumn(index).int
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	protected def model = DocTextDbModel
}

