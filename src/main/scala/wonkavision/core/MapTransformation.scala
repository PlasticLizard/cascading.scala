package org.wonkavision.core

import scala.collection.mutable.Stack
import scala.collection.mutable.HashMap
import org.joda.time.format.ISODateTimeFormat
import org.scala_tools.time.Imports._

trait MapTransformation {

	implicit def toOption(value : Any) = Option(value)

	type Source = Map[String, Any]
	type Target = HashMap[String, Any]

	val sourceStack = new Stack[Source]
	val targetStack = new Stack[Target]

	def source = sourceStack.head
	def target = targetStack.head

	def map : Unit

	def execute( source: Source, t: Target = new Target ): Target =
		to(t){ from(Some(source)){ map } } 

	def from(src: Option[Source],
		       fieldName : Option[String] = None)
		       (f: => Unit) = {

		var context = src.getOrElse(source)
		for (field <- fieldName) {
			context = context.getOrElse(field, context).asInstanceOf[Source]
		}
		withContext[Source](sourceStack, context, f)
	}

	def to(target: Target)(f: => Unit) =
		withContext[Target](targetStack, target, f)
	
	def child(childName: String, source: Any)(f: => Unit) : Unit = {
		child(childName, Some(source.asInstanceOf[Source]))(f)
	}

	def child(childName: String, source: Option[Source] = None)(f: => Unit) {
		target(childName) = to( new Target ) {
			from(source, Some(childName)){f}
		}
	}

	def int(fieldName: String,
		      value: Option[Any] = None,
		      default: Option[Any] = None ) = {
		       
		setTarget(fieldName, Convert toInt getValue(fieldName, value, default))
	}

	def ints(fieldNames : String*) = fieldNames.foreach(int(_))

	def long(fieldName: String,
		       value: Option[Any] = None,
		       default: Option[Any] = None ) = {

		setTarget(fieldName, Convert toLong getValue(fieldName, value, default))
	}
	def longs(fieldNames: String*) = fieldNames.foreach(long(_))

	def double(fieldName: String,
		         value: Option[Any] = None,
		         default: Option[Any] = None) = {

		setTarget(fieldName, Convert toDouble getValue(fieldName, value, default))
	}
	def doubles(fieldNames : String*) = fieldNames.foreach(double(_))

  def string(fieldName: String,
	           value: Option[Any] = None,
	           default: Option[Any] = None) = {

  	setTarget( fieldName, Convert toString getValue(fieldName, value, default))
  }	
	def strings(fieldNames: String*) = fieldNames.foreach(string(_))

	def date(fieldName: String,
				   value: Option[Any] = None,
				   default: Option[Any] = None) = {
				   	
		setTarget( fieldName, Convert toDate getValue(fieldName, value, default))	
	}
	def dates(fieldNames: String*) = fieldNames.foreach(string(_))

	def dateString(fieldName: String,
		             value: Option[Any] = None,
		             default: Option[Any] = None,
		             format: String = "yyyy-MM-dd" ) = {

		val date = Convert toDate getValue(fieldName, value, default)
		var dateString : String = null
		for(d <- date) {
			dateString = DateTimeFormat.forPattern(format).print(d)
		}
		setTarget(fieldName, Option(dateString))
	}

	private def getValue(fieldName: String,
		                   proposedValue: Option[Any],
		                   defaultValue: Option[Any] = None) : Any = {
		
		var default = source.getOrElse(fieldName, null)
		default = defaultValue.getOrElse(default)

		proposedValue getOrElse default
		
	}

	private def setTarget(fieldName : String,
		                    value: Option[Any],
		                    default: Any = null) : Any = {

		target(fieldName) = value.getOrElse(default)
		target(fieldName)	
	}

	private def withContext[T](stack: Stack[T],
		                         ctx: T,
		                         f: => Unit):T = {

		stack.push(ctx); f; stack.pop
	}
	
}