package vf.optidepy.database.factory.library

import utopia.flow.generic.model.immutable.Model
import utopia.flow.generic.model.template.ModelLike.AnyModel
import utopia.vault.nosql.factory.row.model.FromValidatedRowModelFactory
import vf.optidepy.database.props.library.PlacedLinkDbProps

/**
  * Common trait for factories which parse placed link data from database-originated models
  * @tparam A Type of read instances
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait PlacedLinkDbFactoryLike[+A] extends FromValidatedRowModelFactory[A]
{
	// ABSTRACT	--------------------
	
	/**
	  * Database properties used when parsing column data
	  */
	def dbProps: PlacedLinkDbProps
	
	/**
	  * @param model Model from which additional data may be read
	  * @param id Id to assign to the read/parsed placed link
	  * @param parentId parent id to assign to the new placed link
	  * @param childId child id to assign to the new placed link
	  * @param orderIndex order index to assign to the new placed link
	  * @return A placed link with the specified data
	  */
	protected def apply(model: AnyModel, id: Int, parentId: Int, childId: Int, orderIndex: Int): A
	
	
	// IMPLEMENTED	--------------------
	
	override protected def fromValidatedModel(valid: Model) = 
		apply(valid, valid(dbProps.id.name).getInt, valid(dbProps.parentId.name).getInt, 
			valid(dbProps.childId.name).getInt, valid(dbProps.orderIndex.name).getInt)
}

