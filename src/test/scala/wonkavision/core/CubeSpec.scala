package org.wonkavision.core

import org.scalatest.Spec
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers
import scala.io.Source
import org.wonkavision.core._

class CubeSpec extends Spec with BeforeAndAfter with ShouldMatchers {

  var cube : Cube = _

  before {
    cube = Cube (
      
      "mah cube",

      Dimension (
        name = "dimname",
        key = "dimkey",
        caption = "dimcap",
        sort = "dimsort"
      ),

      Dimension (
        name = "dimname2",
        key = "dimkey2",
        caption = "dimcap2",
        sort = "dimsort2"
      )
    )
    
  }

  describe("Instantiation") {
    it("should create a new Cube") {
      cube.name should equal ("mah cube")
      
      cube dimensions "dimname" should equal (
        Dimension ("dimname","dimkey","dimcap","dimsort")
      )
      cube dimensions "dimname2" should equal (
        Dimension ("dimname2","dimkey2","dimcap2","dimsort2")
      )

    }    
  }

}
