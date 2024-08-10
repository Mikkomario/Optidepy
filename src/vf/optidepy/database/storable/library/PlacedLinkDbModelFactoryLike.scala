package vf.optidepy.database.storable.library

import utopia.vault.model.immutable.Storable
import utopia.vault.model.template.FromIdFactory
import utopia.vault.nosql.storable.StorableFactory
import vf.optidepy.model.factory.library.PlacedLinkFactory

/**
  * Common trait for factories used for constructing placed link database models
  * @tparam DbModel Type of database interaction models constructed
  * @tparam A Type of read instances
  * @tparam Data Supported data-part type
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait PlacedLinkDbModelFactoryLike[+DbModel <: Storable, +A, -Data] 
	extends StorableFactory[DbModel, A, Data] with FromIdFactory[Int, DbModel] with PlacedLinkFactory[DbModel]

