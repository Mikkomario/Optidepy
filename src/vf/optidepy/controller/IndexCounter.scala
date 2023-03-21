package vf.optidepy.controller

/**
 * Used for counting build indices
 * @author Mikko Hilpinen
 * @since 20.3.2023, v0.1
 */
class IndexCounter(initial: Int = 1) extends Iterator[Int]
{
	// ATTRIBUTES   -------------------
	
	private var _next = initial
	
	
	// IMPLEMENTED  -------------------
	
	override def hasNext: Boolean = true
	override def next(): Int = {
		val res = _next
		_next += 1
		res
	}
}
