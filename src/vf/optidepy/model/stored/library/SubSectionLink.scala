package vf.optidepy.model.stored.library

import utopia.flow.generic.model.template.ModelLike.AnyModel
import utopia.vault.model.template.StoredFromModelFactory
import vf.optidepy.database.access.single.library.doc.link.DbSingleSubSectionLink
import vf.optidepy.model.factory.library.SubSectionLinkFactoryWrapper
import vf.optidepy.model.partial.library.{PlacedLinkData, SubSectionLinkData}

object SubSectionLink extends StoredFromModelFactory[SubSectionLinkData, SubSectionLink]
{
	// IMPLEMENTED	--------------------
	
	override def dataFactory = SubSectionLinkData
	
	override protected def complete(model: AnyModel, data: SubSectionLinkData) = 
		model("id").tryInt.map { apply(_, data) }
}

/**
  * Represents a sub section link that has already been stored in the database
  * @param id id of this sub section link in the database
  * @param data Wrapped sub section link data
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
// FIXME: Can't extend PlacedLink because of the different Data type
case class SubSectionLink(id: Int, data: SubSectionLinkData) 
	extends SubSectionLinkFactoryWrapper[SubSectionLinkData, SubSectionLink] with PlacedLinkData
		with StoredPlacedLinkLike[SubSectionLinkData, SubSectionLink]
{
	// COMPUTED	--------------------
	
	/**
	  * An access point to this sub section link in the database
	  */
	def access = DbSingleSubSectionLink(id)
	
	
	// IMPLEMENTED	--------------------
	
	override def withId(id: Int): SubSectionLink = copy(id = id)
	
	override protected def wrap(data: SubSectionLinkData) = copy(data = data)
}

