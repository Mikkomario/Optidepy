package vf.optidepy.model.combined.project

import utopia.flow.view.template.Extender
import utopia.vault.model.template.HasId
import vf.optidepy.model.factory.project.ProjectFactoryWrapper
import vf.optidepy.model.partial.project.ProjectData
import vf.optidepy.model.stored.project.Project

/**
  * Common trait for combinations that add additional data to projects
  * @tparam Repr Type of the implementing class
  * @author Mikko Hilpinen
  * @since 10.08.2024, v1.2
  */
trait CombinedProject[+Repr] 
	extends Extender[ProjectData] with HasId[Int] with ProjectFactoryWrapper[Project, Repr]
{
	// ABSTRACT	--------------------
	
	/**
	  * Wrapped project
	  */
	def project: Project
	
	
	// IMPLEMENTED	--------------------
	
	/**
	  * Id of this project in the database
	  */
	override def id = project.id
	
	override def wrapped = project.data
	
	override protected def wrappedFactory = project
}

