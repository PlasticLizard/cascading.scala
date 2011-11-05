package org.wonkavision.cascading.operation

import cascading.flow.FlowProcess
import cascading.operation.{BaseOperation, Function, FunctionCall}
import cascading.tuple.{Fields, Tuple}

class JsonParser (fields : Fields)
	extends BaseOperation[Nothing](fields) with Function[Nothing]
{
	def this() = this(Fields.ARGS)

	@Override
	override def operate( flowProcess : FlowProcess[_], functionCall : FunctionCall[Nothing] ) : Unit = {

		var input = functionCall.getArguments
		var output = functionCall.getOutputCollector

		functionCall.getOutputCollector().add( input.getTuple )
	} 
}