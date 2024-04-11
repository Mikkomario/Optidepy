package vf.optidepy.model.dependency

/**
 * Represents a project's dependency on another project.
 * @author Mikko Hilpinen
 * @since 10.04.2024, v1.2
 */
case class LibraryDependency(libraryId: String, moduleDependencies: Set[ModuleDependency])
