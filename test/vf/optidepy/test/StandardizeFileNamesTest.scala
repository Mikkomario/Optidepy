package vf.optidepy.test

import vf.optidepy.controller.deployment.Standardize
import vf.optidepy.util.Common._
import utopia.flow.parse.file.FileExtensions._
import utopia.flow.parse.string.Regex
import utopia.flow.util.StringExtensions._

/**
 * Tests the file name standardization algorithm
 *
 * @author Mikko Hilpinen
 * @since 07.05.2024, v1.2
 */
object StandardizeFileNamesTest extends App
{
	private val dashRegex = Regex.escape('-')
	private val acceptedCharRegex = (Regex.letterOrDigit || dashRegex).withinParenthesis
	
	assert(dashRegex("-"))
	assert(!dashRegex("t"))
	assert(acceptedCharRegex("1"))
	assert(!acceptedCharRegex("["))
	
	assert("test string".replaceAllExcept(acceptedCharRegex, "-") == "test-string",
		"test string".replaceAllExcept(acceptedCharRegex, "-"))
	
	Standardize.fileNamesUnder("data/test-data")
	println("Done!")
}
