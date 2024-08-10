package vf.optidepy.model.stored.deployment

import utopia.flow.generic.model.template.ModelLike.AnyModel
import utopia.vault.model.template.{FromIdFactory, StoredFromModelFactory, StoredModelConvertible}
import vf.optidepy.database.access.single.deployment.branch.DbSingleBranch
import vf.optidepy.model.factory.deployment.BranchFactoryWrapper
import vf.optidepy.model.partial.deployment.BranchData

object Branch extends StoredFromModelFactory[BranchData, Branch]
{
	// IMPLEMENTED	--------------------
	
	override def dataFactory = BranchData
	
	override protected def complete(model: AnyModel, data: BranchData) = model("id").tryInt.map { apply(_, 
		data) }
}

/**
  * Represents a branch that has already been stored in the database
  * @param id id of this branch in the database
  * @param data Wrapped branch data
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
case class Branch(id: Int, data: BranchData) 
	extends StoredModelConvertible[BranchData] with FromIdFactory[Int, Branch] 
		with BranchFactoryWrapper[BranchData, Branch]
{
	// COMPUTED	--------------------
	
	/**
	  * An access point to this branch in the database
	  */
	def access = DbSingleBranch(id)
	
	
	// IMPLEMENTED	--------------------
	
	override protected def wrappedFactory = data
	
	override def withId(id: Int) = copy(id = id)
	
	override protected def wrap(data: BranchData) = copy(data = data)
}

