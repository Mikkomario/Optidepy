package vf.optidepy.model.factory.deployment

import java.nio.file.Path

/**
  * Common trait for binding-related factories which allow construction with individual properties
  * @tparam A Type of constructed instances
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait BindingFactory[+A]
{
	// ABSTRACT	--------------------
	
	/**
	  * @param configId New config id to assign
	  * @return Copy of this item with the specified config id
	  */
	def withConfigId(configId: Int): A
	
	/**
	  * @param source New source to assign
	  * @return Copy of this item with the specified source
	  */
	def withSource(source: Path): A
	
	/**
	  * @param target New target to assign
	  * @return Copy of this item with the specified target
	  */
	def withTarget(target: Path): A
}

