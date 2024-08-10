package vf.optidepy.database.storable.library

import utopia.vault.model.immutable.Table
import vf.optidepy.database.props.library.PlacedLinkDbProps

object PlacedLinkDbModel
{
	// OTHER	--------------------
	
	/**
	  * @param table The primarily targeted table
	  * @param props Targeted database properties
	  * @return A factory used for constructing placed link models using the specified configuration
	  */
	def factory(table: Table, props: PlacedLinkDbProps) = PlacedLinkDbModelFactory(table, props)
}

/**
  * Common trait for database interaction models dealing with placed links
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait PlacedLinkDbModel extends PlacedLinkDbModelLike[PlacedLinkDbModel]

