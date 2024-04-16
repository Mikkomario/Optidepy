package vf.optidepy.model.library

import utopia.flow.time.Now
import utopia.flow.util.Version

import java.time.Instant

/**
 * Represents an export / release of a (library) module
 * @param version Released version
 * @param changeDoc Documentation of this version's changes
 * @param timestamp Time when this release was created
 * @author Mikko Hilpinen
 * @since 15.04.2024, v1.2
 */
case class ModuleRelease(version: Version, changeDoc: DocSection = DocSection.empty, timestamp: Instant = Now)
