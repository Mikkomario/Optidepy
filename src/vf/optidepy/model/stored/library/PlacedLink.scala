package vf.optidepy.model.stored.library

import utopia.flow.generic.model.template.ModelLike.AnyModel
import utopia.vault.model.template.StoredFromModelFactory
import vf.optidepy.model.partial.library.PlacedLinkData

object PlacedLink extends StoredFromModelFactory[PlacedLinkData, PlacedLink]
{
	// IMPLEMENTED	--------------------
	
	override def dataFactory = PlacedLinkData
	
	override protected def complete(model: AnyModel, data: PlacedLinkData) = 
		model("id").tryInt.map { apply(_, data) }
	
	
	// OTHER	--------------------
	
	/**
	  * Creates a new placed link
	  * @param id id of this placed link in the database
	  * @param data Wrapped placed link data
	  * @return placed link with the specified id and wrapped data
	  */
	def apply(id: Int, data: PlacedLinkData): PlacedLink = _PlacedLink(id, data)
	
	
	// NESTED	--------------------
	
	/**
	  * Concrete implementation of the placed link trait
	  * @param id id of this placed link in the database
	  * @param data Wrapped placed link data
	  * @author Mikko Hilpinen
	  * @since 09.08.2024
	  */
	private case class _PlacedLink(id: Int, data: PlacedLinkData) extends PlacedLink
	{
		// IMPLEMENTED	--------------------
		
		override def withId(id: Int): PlacedLink = copy(id = id)
		
		override protected def wrap(data: PlacedLinkData) = copy(data = data)
	}
}

/**
  * Represents a placed link that has already been stored in the database
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait PlacedLink extends StoredPlacedLinkLike[PlacedLinkData, PlacedLink] with PlacedLinkData

