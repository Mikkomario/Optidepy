package vf.optidepy.model.factory.deployment

import vf.optidepy.model.template.deployment.FromBindingPathsFactory

/**
  * Common trait for binding-related factories which allow construction with individual properties
  * @tparam A Type of constructed instances
  * @author Mikko Hilpinen
  * @since 09.08.2024, v1.2
  */
trait BindingFactory[+A] extends FromBindingPathsFactory[A]
{
	// ABSTRACT	--------------------
	
	/**
	  * @param configId New config id to assign
	  * @return Copy of this item with the specified config id
	  */
	def withConfigId(configId: Int): A
}

