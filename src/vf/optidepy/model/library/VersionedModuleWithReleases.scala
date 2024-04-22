package vf.optidepy.model.library

import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.factory.FromModelFactory
import utopia.flow.generic.model.immutable.{Constant, Model}
import utopia.flow.generic.model.template.{ModelConvertible, ModelLike, Property}
import utopia.flow.view.template.Extender

import scala.util.Try

object VersionedModuleWithReleases extends FromModelFactory[VersionedModuleWithReleases]
{
	override def apply(model: ModelLike[Property]): Try[VersionedModuleWithReleases] =
		VersionedModule(model).flatMap { module =>
			model("releases").tryVectorWith { v => ModuleRelease(v.getModel) }.map { apply(module, _) }
		}
}

/**
 * Attaches released module versions to a library module
 * @author Mikko Hilpinen
 * @since 15.04.2024, v1.2
 */
case class VersionedModuleWithReleases(module: VersionedModule, releases: Vector[ModuleRelease])
	extends Extender[VersionedModule] with ModelConvertible
{
	// IMPLEMENTED  ------------------------
	
	override def wrapped: VersionedModule = module
	
	override def toModel: Model = module.toModel + Constant("releases", releases)
}
