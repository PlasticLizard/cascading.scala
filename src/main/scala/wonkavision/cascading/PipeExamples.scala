package org.wonkavision.cascading

import cascading.tuple.Fields
import cascading.scheme.local.{TextLine, TextDelimited}
import java.util.Properties
import cascading.tap.SinkMode
import cascading.tap.local.FileTap
import cascading.flow.local.LocalFlowConnector
import cascading.flow.FlowConnector
import cascading.pipe.Each
import cascading.operation.Identity


object PipeExamples{
  def main(args: Array[String]) {
    val properties = new Properties()
    FlowConnector.setApplicationJarClass( properties, PipeExamples.getClass )
    val flowConnector = new LocalFlowConnector(properties)

    val fileOneFields = new Fields("file1 field1", "file1 field2", "file1 field3")
    val sourcePath = args(0)
    val sourceScheme = new TextDelimited(fileOneFields, false, "\t")
    val sourceTap = new FileTap(sourceScheme, sourcePath)

    val fileTwoFields = new Fields("file2 field1", "file2 field2", "file2 field3")
    val sourceTwoPath = args(1)
    val sourceTwoScheme = new TextDelimited(fileTwoFields, false, "\t")
    val sourceTwoTap = new FileTap(sourceTwoScheme, sourceTwoPath)

    val sinkPath = args(2)
    val sinkTap = new FileTap(new TextLine(), sinkPath, SinkMode.REPLACE )

    //BEGIN APPLICATION CODE

    val pipe = new Each("identity", new Fields( "file1 field1"), new Identity())

    //END APPLICATION CODE

    val flow = flowConnector.connect( sourceTap, sinkTap, pipe )

    // optionally print out the parsedLogFlow to a DOT file for import into a graphics package
    //flow.writeDOT( "pipeExamples.dot" )

    flow.complete()
  }
}
