package rpm_analytics.transformations

import org.wonkavision.core.MapTransformation
import org.wonkavision.core.Convert
import org.scala_tools.time.Imports._

class WorkQueueEntryDynamic extends MapTransformation {   
		
	def map() = map(DateTime.now)

	def map(contextTime : DateTime = DateTime.now) {
		
		val contextDate = formatDate (contextTime, ISO_DAY)
		val startDate =   formatDate ("due_date")
		val endDate =     formatDate ("completed_time")
		val overdueDate = formatDate ("overdue_date")
		
		val isActive = contextDate >= startDate &&
		               (endDate == null || contextDate <= endDate)

		bool ("is_active", isActive)

		count ("available") { isActive == true && endDate == null }
		
		count ("expiring_today") {
			overdueDate != null && 
			overdueDate == contextDate &&
			(endDate == null || overdueDate < endDate)
		}

		count ("incoming") { startDate == contextDate }
		count ("outgoing") { endDate == contextDate }
		
		count ("completed") {
			endDate != null && endDate == contextDate &&
			getString("resolution", default = "").get != "completed"
		}
		
		count ("cancelled") {
			endDate != null && endDate == contextDate &&
			getString("resolution", default = "").get != "completed"
		}

		count("overdue") {
			isActive && overdueDate != null && overdueDate < contextDate &&
			endDate != contextDate &&
			(endDate == null || overdueDate < endDate)
		}
		
		child ("status") {
			string ("status", detectStatus(contextDate, startDate, endDate))
			string ("sort", statusSort)	
		}

	}


	def formatDate(fieldName: String, format: String = ISO_DAY) : String =
		formatDate(getDate(fieldName), format)

	def count(fieldName : String, inc : Int = 1)( pred : => Boolean ) {
		val c = if (pred) inc else null
		int(fieldName, c)
	}

	def detectStatus(contextDate : String, startDate : String, endDate : String) {
		if (contextDate < startDate) {
			"scheduled"
		} else if (endDate != null && endDate < contextDate) {
			"closed"
		} else if (target("completed") != null) {
			"completed"
		} else if (target("cancelled") != null) {
			"cancelled"
		} else if (target("overdue") != null) {
			"overdue"
		} else {
			"ready"
		}
	}

	def statusSort = 1

}
