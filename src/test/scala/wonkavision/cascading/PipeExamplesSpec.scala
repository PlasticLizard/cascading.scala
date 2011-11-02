package org.wonkavision.cascading

import org.scalatest.Spec
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers
import scala.io.Source

class PipeExamplesSpec extends Spec with BeforeAndAfter with ShouldMatchers {

  var result: List[String] = _

  before {
    val input1 = "src/test/resources/input/pipe_examples_file1.txt"
    val input2 = "src/test/resources/input/pipe_examples_file2.txt"
    val output = "./tmp/output/out.txt"

    PipeExamples.main(Array(input1, input2, output))

    result = Source.fromFile(output).getLines.toList
  }

  describe("Processing simple input streams") {
    it("should produce an output stream") {
      result should have length 2
      result(0) should equal ("file 1 : 1st row: 1st col")
      result(1) should equal ("file 1 : 2nd row: 1st col")
    }    
  }

}
