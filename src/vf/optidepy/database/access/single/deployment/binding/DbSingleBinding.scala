package vf.optidepy.database.access.single.deployment.binding

import utopia.vault.nosql.access.single.model.distinct.SingleIntIdModelAccess
import vf.optidepy.model.stored.deployment.Binding

/**
  * An access point to individual bindings, based on their id
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class DbSingleBinding(id: Int) extends UniqueBindingAccess with SingleIntIdModelAccess[Binding]

