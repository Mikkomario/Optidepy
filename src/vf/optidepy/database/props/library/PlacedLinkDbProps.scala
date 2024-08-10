package vf.optidepy.database.props.library

import utopia.vault.model.immutable.{DbPropertyDeclaration, Table}
import utopia.vault.model.template.HasIdProperty

object PlacedLinkDbProps
{
	// OTHER	--------------------
	
	/**
	  * @param table Table operated using this configuration
	  * @param parentIdPropName Name of the database property matching parent id (default = "parentId")
	  * @param childIdPropName Name of the database property matching child id (default = "childId")
	  * @param orderIndexPropName Name of the database property matching order index (default = "orderIndex")
	  * @return A model which defines all placed link database properties
	  */
	def apply(table: Table, parentIdPropName: String = "parentId", childIdPropName: String = "childId", 
		orderIndexPropName: String = "orderIndex"): PlacedLinkDbProps = 
		_PlacedLinkDbProps(table, parentIdPropName, childIdPropName, orderIndexPropName)
	
	
	// NESTED	--------------------
	
	/**
	  * @param table Table operated using this configuration
	  * @param parentIdPropName Name of the database property matching parent id (default = "parentId")
	  * @param childIdPropName Name of the database property matching child id (default = "childId")
	  * @param orderIndexPropName Name of the database property matching order index (default = "orderIndex")
	  */
	private case class _PlacedLinkDbProps(table: Table, parentIdPropName: String = "parentId",
	                                      childIdPropName: String = "childId",
	                                      orderIndexPropName: String = "orderIndex")
		extends PlacedLinkDbProps
	{
		// ATTRIBUTES	--------------------
		
		override lazy val id: DbPropertyDeclaration = DbPropertyDeclaration("id", index)
		override lazy val parentId = DbPropertyDeclaration.from(table, parentIdPropName)
		override lazy val childId = DbPropertyDeclaration.from(table, childIdPropName)
		override lazy val orderIndex = DbPropertyDeclaration.from(table, orderIndexPropName)
	}
}

/**
  * Common trait for classes which provide access to placed link database properties
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait PlacedLinkDbProps extends HasIdProperty
{
	// ABSTRACT	--------------------
	
	/**
	  * Declaration which defines how parent id shall be interacted with in the database
	  */
	def parentId: DbPropertyDeclaration
	/**
	  * Declaration which defines how child id shall be interacted with in the database
	  */
	def childId: DbPropertyDeclaration
	/**
	  * Declaration which defines how order index shall be interacted with in the database
	  */
	def orderIndex: DbPropertyDeclaration
}

