package notebookProcessor

import util.Clock
import util.IncorrectDateFormat
import util.IncorrectTimeFormat
import util.Property
import java.util.*

class Note {
    val properties: MutableList<Property> = mutableListOf()
        get() {
            properties.sort()
            return field
        }
    var message: String = "Zzz..."

    var priority = 1
    var id: Long = 0
    var date = Date()
    var location: String = "Far from any road..."
    var section: String = ""
    var time: Clock = Clock(0, 0)

    fun addProperty(newProperty: Property, propertyValue: String) {
        properties.add(newProperty)
        when (newProperty) {
            Property.RATING -> priority = propertyValue.toInt()
            Property.DATE -> {
                val parsedDate = propertyValue.split(".")
                if (parsedDate.size != 3) throw IncorrectDateFormat(propertyValue)
                date = Date(parsedDate[2].toInt(), parsedDate[1].toInt(), parsedDate[0].toInt())
            }
            Property.LOCATION -> location = propertyValue
            Property.SECTION -> section = propertyValue
            Property.TIME -> {
                val parsedTime = propertyValue.split(":")
                if (parsedTime.size != 2) throw IncorrectTimeFormat(propertyValue)
                val hours = parsedTime[0].toInt()
                val minutes = parsedTime[1].toInt()
                if (hours > 23 || hours < 0 || minutes > 59 || minutes < 0) throw IncorrectTimeFormat(propertyValue)
                time = Clock(hours, minutes)
            }
        }
    }
}

