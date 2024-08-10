package vf.optidepy.model.stored.library

import utopia.flow.generic.model.template.ModelLike.AnyModel
import utopia.vault.model.template.StoredFromModelFactory
import vf.optidepy.database.access.single.library.doc.link.DbSingleDocLineLink
import vf.optidepy.model.factory.library.DocLineLinkFactoryWrapper
import vf.optidepy.model.partial.library.{DocLineLinkData, PlacedLinkData}

object DocLineLink extends StoredFromModelFactory[DocLineLinkData, DocLineLink]
{
	// IMPLEMENTED	--------------------
	
	override def dataFactory = DocLineLinkData
	
	override protected def complete(model: AnyModel, data: DocLineLinkData) = 
		model("id").tryInt.map { apply(_, data) }
}

/**
  * Represents a doc line link that has already been stored in the database
  * @param id id of this doc line link in the database
  * @param data Wrapped doc line link data
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DocLineLink(id: Int, data: DocLineLinkData) 
	extends PlacedLinkData with DocLineLinkFactoryWrapper[DocLineLinkData, DocLineLink]
		with StoredPlacedLinkLike[DocLineLinkData, DocLineLink]
{
	// COMPUTED	--------------------
	
	/**
	  * An access point to this doc line link in the database
	  */
	def access = DbSingleDocLineLink(id)
	
	
	// IMPLEMENTED	--------------------
	
	override protected def wrap(data: DocLineLinkData) = copy(data = data)
	
	override def withId(id: Int): DocLineLink = copy(id = id)
}

