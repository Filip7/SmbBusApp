package tvz.filip.milkovic.smbraspored.shared.model.service.impl

import tvz.filip.milkovic.smbraspored.shared.model.Model
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.math.abs

class ModelServiceImpl {
    companion object {

        fun getNextDeparture(
            busLine: Model.BusLine,
            startingPointIsFirstListed: Int
        ): Model.Departure? {
            val departures = busLine.departures ?: return null
            var currentTime = LocalDateTime.now().toLocalTime()


            var returnVal = getDeparture(departures, currentTime, startingPointIsFirstListed)


            if (returnVal == null) {
                currentTime = LocalTime.MIDNIGHT
                returnVal = getDeparture(departures, currentTime, startingPointIsFirstListed)
            }

            return returnVal
        }

        private fun getDeparture(
            departures: List<Model.Departure>,
            currentTime: LocalTime,
            startingPointIsFirstListed: Int
        ): Model.Departure? {
            return departures.filter {
                it.startingPointIsFirstListed == startingPointIsFirstListed
            }.filter {
                it.getDepartureTimeInLocalDateTime().isAfter(currentTime)
            }.sortedWith(compareBy {
                it.departureTime
            }).minWith(compareBy {
                abs(it.getDepartureTimeInLocalDateTime().toNanoOfDay() - currentTime.toNanoOfDay())
            })
        }
    }

}
