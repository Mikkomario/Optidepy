package vf.optidepy.model.factory.deployment

import utopia.flow.util.Version

import java.time.Instant

/**
  * Common trait for deployment-related factories which allow construction with individual properties
  * @tparam A Type of constructed instances
  * @author Mikko Hilpinen
  * @since 24.08.2024, v1.2
  */
trait DeploymentFactory[+A]
{
	// ABSTRACT	--------------------
	
	/**
	  * @param branchId New branch id to assign
	  * @return Copy of this item with the specified branch id
	  */
	def withBranchId(branchId: Int): A
	
	/**
	  * @param created New created to assign
	  * @return Copy of this item with the specified created
	  */
	def withCreated(created: Instant): A
	
	/**
	  * @param deploymentIndex New deployment index to assign
	  * @return Copy of this item with the specified deployment index
	  */
	def withDeploymentIndex(deploymentIndex: Int): A
	
	/**
	  * @param latestUntil New latest until to assign
	  * @return Copy of this item with the specified latest until
	  */
	def withLatestUntil(latestUntil: Instant): A
	
	/**
	  * @param version New version to assign
	  * @return Copy of this item with the specified version
	  */
	def withVersion(version: Version): A
}

