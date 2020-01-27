package tvz.filip.milkovic.smbraspored.shared.model.service.impl

import tvz.filip.milkovic.smbraspored.shared.model.Model
import tvz.filip.milkovic.smbraspored.shared.model.TypeOfDay
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
            val currentDay = LocalDateTime.now().dayOfWeek

            var returnVal = getDeparture(
                departures, currentTime, startingPointIsFirstListed,
                TypeOfDay.getTypeOfDayFromCurrentDay(currentDay)
            )

            if (returnVal == null) {
                currentTime = LocalTime.MIDNIGHT
                returnVal = getDeparture(
                    departures, currentTime, startingPointIsFirstListed,
                    TypeOfDay.getNextDay(currentDay)
                )
            }

            return returnVal
        }

        private fun getDeparture(
            departures: List<Model.Departure>,
            currentTime: LocalTime,
            startingPointIsFirstListed: Int,
            typeOfDay: TypeOfDay
        ): Model.Departure? {
            return departures.filter {
                it.startingPointIsFirstListed == startingPointIsFirstListed
            }.filter {
                it.typeOfDay == typeOfDay
            }.filter {
                it.getDepartureTimeInLocalTime().isAfter(currentTime)
            }.sortedWith(compareBy {
                it.departureTime
            }).minWith(compareBy {
                abs(it.getDepartureTimeInLocalTime().toNanoOfDay() - currentTime.toNanoOfDay())
            })
        }
    }

}
