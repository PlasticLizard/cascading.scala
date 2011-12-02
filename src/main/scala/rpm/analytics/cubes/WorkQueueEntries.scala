package rpm.analytics.cubes

import org.wonkavision.core._

class WorkQueueEntries extends Cube("Work Queue Entries") {
	
	dimension (
		name = "team",
		key = "id",
		caption = "name",
		sort = "name"
	)

	dimension (
		name = "status",
		key = "status",
		caption = "status",
		sort = "sort"
	)

	dimension (
		name = "assigned_to",
		key = "id",
		caption = "name",
		sort = "name"
	)

	dimension (
		name = "work_queue_priority",
		key = "key",
		caption = "name",
		sort = "name"
	)

	dimension (
		name = "work_queue",
		key = "id",
		caption = "name",
		sort = "priority"
	)

	sum ( "incoming",
		    "outgoing",
		    "completed",
		    "overdue",
		    "expiring_today",
		    "available"
	)

	calc ("change", format = MeasureFormat.Integer) {
		//incoming - outgoing
		() => 1.0
	}

	calc ("ready", format = MeasureFormat.Integer) {
		//available - expiring_today - overdue
		() => 1.0
	}

	//alias ("available", as = "total_open", format = MeasureFormat.Integer)

	aggregation (

		name = "Rpm::WorkQueueStatus",

		measures = List("count", "incoming", "outgoing", "completed", "overdue",
		                "expiring_today", "available", "change", "ready"),

		_.combine( "team", "work_queue", "work_queue_priority" )
		 .add ( "team", "status" )
		 .add ( "team", "work_queue", "status" )
		 .add (" team", "work_queue_priority", "work_queue", "status" )
		 .add ( "work_queue_priority", "status" )
	)

}