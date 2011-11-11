package org.wonkavision.core

case class Cube(
	val name : String,
	val dimensions : Map[String, Dimension]
)

object Cube {
	def apply(name : String, dimensions : Dimension*) =	 {
		val dims : Map[String, Dimension] =
			dimensions map { d => ( d.name,  d ) } toMap 
		val c = new Cube(name, dims)
		c
	}
}