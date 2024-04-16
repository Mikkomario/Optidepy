package vf.optidepy.model.library

import utopia.flow.operator.MaybeEmpty

object DocSection
{
	/**
	 * Empty documentation
	 */
	val empty = apply("")
}

/**
 * Represents a chapter or a section within a documentation
 *
 * @param header Header assigned to this section
 * @param lines Lines of text following the section header
 * @param subSections Inner sections within this section
 * @author Mikko Hilpinen
 * @since 15.04.2024, v1.2
 */
case class DocSection(header: String, lines: Vector[String] = Vector.empty,
                      subSections: Vector[DocSection] = Vector.empty)
	extends MaybeEmpty[DocSection]
{
	override def self: DocSection = this
	override def isEmpty: Boolean = header.isEmpty && lines.isEmpty && subSections.isEmpty
}