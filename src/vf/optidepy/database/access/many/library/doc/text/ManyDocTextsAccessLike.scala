package vf.optidepy.database.access.many.library.doc.text

import utopia.flow.collection.immutable.IntSet
import utopia.flow.generic.casting.ValueConversions._
import utopia.vault.database.Connection
import utopia.vault.nosql.access.many.model.ManyModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.FilterableView
import vf.optidepy.database.storable.library.DocTextDbModel

/**
  * A common trait for access points which target multiple doc texts or similar instances at a time
  * @tparam A Type of read (doc texts -like) instances
  * @tparam Repr Type of this access point
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait ManyDocTextsAccessLike[+A, +Repr] extends ManyModelAccess[A] with Indexed with FilterableView[Repr]
{
	// COMPUTED	--------------------
	
	/**
	  * texts of the accessible doc texts
	  */
	def texts(implicit connection: Connection) = pullColumn(model.text.column).flatMap { _.string }
	
	/**
	  * indentations of the accessible doc texts
	  */
	def indentations(implicit connection: Connection) = pullColumn(model.indentation.column)
		.map { v => v.getInt }
	
	/**
	  * creation times of the accessible doc texts
	  */
	def creationTimes(implicit connection: Connection) = 
		pullColumn(model.created.column).map { v => v.getInstant }
	
	/**
	  * Unique ids of the accessible doc texts
	  */
	def ids(implicit connection: Connection) = pullColumn(index).map { v => v.getInt }
	
	/**
	  * Model which contains the primary database properties interacted with in this access point
	  */
	protected def model = DocTextDbModel
	
	
	// OTHER	--------------------
	
	/**
	  * @param indentation indentation to target
	  * @return Copy of this access point that only includes doc texts with the specified indentation
	  */
	def withIndentation(indentation: Int) = filter(model.indentation.column <=> indentation)
	
	/**
	  * @param indentations Targeted indentations
	  * 
		@return Copy of this access point that only includes doc texts where indentation is within the specified
	  *  value set
	  */
	def withIndentations(indentations: Iterable[Int]) = 
		filter(model.indentation.column.in(IntSet.from(indentations)))
	
	/**
	  * @param text text to target
	  * @return Copy of this access point that only includes doc texts with the specified text
	  */
	def withText(text: String) = filter(model.text.column <=> text)
	
	/**
	  * @param texts Targeted texts
	  * 
		@return Copy of this access point that only includes doc texts where text is within the specified value set
	  */
	def withTexts(texts: Iterable[String]) = filter(model.text.column.in(texts))
}

