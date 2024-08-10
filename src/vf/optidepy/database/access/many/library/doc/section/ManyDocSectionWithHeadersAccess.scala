package vf.optidepy.database.access.many.library.doc.section

import utopia.vault.database.Connection
import utopia.vault.nosql.access.many.model.ManyRowModelAccess
import utopia.vault.nosql.view.ViewFactory
import utopia.vault.sql.Condition
import vf.optidepy.database.factory.library.DocSectionWithHeaderDbFactory
import vf.optidepy.database.storable.library.DocTextDbModel
import vf.optidepy.model.combined.library.DocSectionWithHeader

object ManyDocSectionWithHeadersAccess extends ViewFactory[ManyDocSectionWithHeadersAccess]
{
	// IMPLEMENTED	--------------------
	
	/**
	  * @param condition Condition to apply to all requests
	  * @return An access point that applies the specified filter condition (only)
	  */
	override def apply(condition: Condition): ManyDocSectionWithHeadersAccess = 
		_ManyDocSectionWithHeadersAccess(Some(condition))
	
	
	// NESTED	--------------------
	
	private case class _ManyDocSectionWithHeadersAccess(override val accessCondition: Option[Condition]) 
		extends ManyDocSectionWithHeadersAccess
}

/**
  * A common trait for access points that return multiple doc section with headers at a time
  * @author Mikko Hilpinen
  * @since 09.08.2024
  */
trait ManyDocSectionWithHeadersAccess 
	extends ManyDocSectionsAccessLike[DocSectionWithHeader, ManyDocSectionWithHeadersAccess] 
		with ManyRowModelAccess[DocSectionWithHeader]
{
	// COMPUTED	--------------------
	
	/**
	  * texts of the accessible doc texts
	  */
	def headerTexts(implicit connection: Connection) = pullColumn(headerModel.text.column)
		.flatMap { _.string }
	
	/**
	  * indentations of the accessible doc texts
	  */
	def headerIndentations(implicit connection: Connection) = 
		pullColumn(headerModel.indentation.column).map { v => v.getInt }
	
	/**
	  * creation times of the accessible doc texts
	  */
	def headerCreationTimes(implicit connection: Connection) = 
		pullColumn(headerModel.created.column).map { v => v.getInstant }
	
	/**
	  * Model (factory) used for interacting the doc texts associated with this doc section with header
	  */
	protected def headerModel = DocTextDbModel
	
	
	// IMPLEMENTED	--------------------
	
	override def factory = DocSectionWithHeaderDbFactory
	
	override protected def self = this
	
	override def apply(condition: Condition): ManyDocSectionWithHeadersAccess = 
		ManyDocSectionWithHeadersAccess(condition)
}

