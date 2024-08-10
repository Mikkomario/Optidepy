package vf.optidepy.database.access.single.library.doc.section

import utopia.vault.nosql.access.single.model.SingleRowModelAccess
import utopia.vault.nosql.template.Indexed
import utopia.vault.nosql.view.UnconditionalView
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.DocSectionWithHeaderDbFactory
import vf.optidepy.database.storable.library.{DocSectionDbModel, DocTextDbModel}
import vf.optidepy.model.combined.library.DocSectionWithHeader

/**
  * Used for accessing individual doc section with headers
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
object DbDocSectionWithHeader 
	extends SingleRowModelAccess[DocSectionWithHeader] with UnconditionalView with Indexed
{
	// COMPUTED	--------------------
	
	/**
	  * A database model (factory) used for interacting with linked sections
	  */
	protected def model = DocSectionDbModel
	
	/**
	  * A database model (factory) used for interacting with the linked header
	  */
	protected def headerModel = DocTextDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = DocSectionWithHeaderDbFactory
	
	
	// OTHER	--------------------
	
	/**
	  * @param id Database id of the targeted doc section with header
	  * @return An access point to that doc section with header
	  */
	def apply(id: Int) = DbSingleDocSectionWithHeader(id)
	
	/**
	  * 
		@param condition Filter condition to apply in addition to this root view's condition. Should yield unique doc section 
	  * with headers.
	  * @return An access point to the doc section with header that satisfies the specified condition
	  */
	protected def distinct(condition: Condition) = UniqueDocSectionWithHeaderAccess(condition)
}

