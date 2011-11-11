package org.wonkavision.core

import org.scalatest.Spec
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.ShouldMatchers
import scala.io.Source
import scala.collection.mutable.HashMap
import net.liftweb.json._
import net.liftweb.json.ext.JodaTimeSerializers
import org.wonkavision.core._


class MapTransformationSpec extends Spec with BeforeAndAfter with ShouldMatchers {
  
  implicit val formats = net.liftweb.json.DefaultFormats ++ JodaTimeSerializers.all

	var source : Map[String,Any] = _
	var result : HashMap[String, Any] = _

  before {
	  result = new TestTransform().execute(getTestMessage)
  }

  describe("Executing a transformation") {
  	it ("should copy a child map") {
  		result("team") should equal (
	  		Map("id" -> "4de3f6b30c7dea6e3c000103", "name" -> "Ground 1")
	  	)
  	}

  	it ("should map inline strings") {
  		result("id") should equal ("4ea9df2dce47d016090000f5")
  		result("resolution") should equal ("queued")
  	}

  	it ("should map an inline integer") {
  		result("priority") should equal (23)
  	}

  	it ("should map an inline double") {
  		result("current_balance") should equal(507.55)
  	}

    it("should map a child from a nested source") {
      result("current_payer") should equal(
        Map(
          "name" -> "Employers Mutual Inc, AMR - VH",
          "payer_type" -> "Contract",
          "payer_id" -> "4d360a310c7dea643e00302e"
        )
      )
    }

    it("should map a child from an alternate source"){
      result("work_queue_priority") should equal (
        Map(
          "name" -> "Priority 6",
          "priority" -> 6,
          "key" -> "6"
        )
      )
    }

    it("should be able to create a child from the root source") {
      result("company") should equal (
        Map("company_id" -> "4d38c63d0c7dea49e0000005")
      )
    }

    it("should map date fields"){
      result("created_date") should equal (
        Map(
          "date" -> Convert.toDate("2011-10-27T22:46:06Z").get,
          "day_key" -> "2011-10-27"
        )
      )
    }
  }

  def getTestMessage = {
  	val source = scala.io.Source.fromFile("src/test/resources/map_test_message.json")
		val json = source .mkString
		source.close ()
		parse(json).values.asInstanceOf[Map[String,Any]]
  }

}

class TestTransform extends MapTransformation {
	def	map = {
		
    string  ("id")
    string  ("resolution", default="queued")
		int     ("priority")
    double  ("current_balance")

		child ("team") {
			strings ("id", "name")
		}	

    child ("current_payer", source("context")) {
      strings ("name", "payer_type", "payer_id")
    }

    child ("work_queue_priority", source("work_queue")){
      int ("priority")
      string ("name", "Priority " + source("priority"))
      string ("key", source("priority"))
    }

    child ("company") {
      string ("company_id")
    }

    child ("created_date") {
      date ("date", source("created_at"))
      dateString ("day_key", target("date"))
    }
	}



}