package vf.optidepy.model.library

import utopia.flow.collection.CollectionExtensions._
import utopia.flow.collection.mutable.iterator.{OptionsIterator, PollingIterator}
import utopia.flow.generic.casting.ValueConversions._
import utopia.flow.generic.factory.FromModelFactory
import utopia.flow.generic.model.immutable.Model
import utopia.flow.generic.model.template.{ModelConvertible, ModelLike, Property}
import utopia.flow.operator.MaybeEmpty

import scala.util.Try

object DocSection extends FromModelFactory[DocSection]
{
	// ATTRIBUTES   -----------------------
	
	/**
	 * Empty documentation
	 */
	val empty = apply("")
	
	
	// IMPLEMENTED  -----------------------
	
	override def apply(model: ModelLike[Property]): Try[DocSection] =
		model("subSections").getVector.tryMap { v => apply(v.getModel) }.map { subSections =>
			apply(model("header").getString, model("lines").getVector.map { _.getString }, subSections)
		}
	
	
	// OTHER    ---------------------------
	
	/**
	 * Reads a document section from markdown document contents
	 * @param header Header for this section
	 * @param linesIter Iterator that returns the remaining lines.
	 *                  Placed to the first line after the 'header' line.
	 * @param currentDepth Current header depth.
	 *                     E.g. If header started with "##", depth would be 2.
	 *                     Default = 1.
	 * @return Parsed documentation
	 */
	def fromMarkDown(header: String, linesIter: PollingIterator[String], currentDepth: Int = 1): DocSection = {
		val lines = linesIter.collectUntil { _.startsWith("#") }
		val subSections = OptionsIterator.continually(linesIter.pollOption)
			.map { l => l -> lineDepth(l) }
			.takeWhile { _._2 > currentDepth }
			.map { case (line, depth) =>
				linesIter.skipPolled()
				fromMarkDown(line.drop(depth).trim, linesIter, depth)
			}
			.toVector
		apply(header, lines, subSections)
	}
	
	private def lineDepth(line: String) = line.iterator.takeWhile { _ == '#' }.size
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
	extends MaybeEmpty[DocSection] with ModelConvertible
{
	override def self: DocSection = this
	override def isEmpty: Boolean = header.isEmpty && lines.isEmpty && subSections.isEmpty
	
	override def toModel: Model = Model.from("header" -> header, "lines" -> lines, "subSections" -> subSections)
}