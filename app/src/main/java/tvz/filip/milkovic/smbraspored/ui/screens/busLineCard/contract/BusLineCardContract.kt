package tvz.filip.milkovic.smbraspored.ui.screens.busLineCard.contract

import android.app.Activity
import android.widget.CheckBox
import tvz.filip.milkovic.smbraspored.shared.model.Model
import java.time.LocalTime

interface BusLineCardContract {

    interface BusLineCardView

    interface BusLineCardPresenter {
        fun checkIfBusLineIsInTheFavourites(busLine: Model.BusLine): Boolean
        fun selectTimeAndCallAlarm(time: LocalTime, activity: Activity)
        fun addOrRemoveFromFavourites(checkBox: CheckBox, busLineId: Long)
        fun getNextDeparture(
            busLine: Model.BusLine,
            startingPointIsFirstListed: Int
        ): Model.Departure?

        fun getNextDepartureFromRootText(busLineName: String, departureTime: String): String
        fun getNextDepartureFromDestText(busLineName: String, departureTime: String): String
    }

    interface BusLineCardModel {
        fun checkIfBusLineIsInTheFavourites(busLine: Model.BusLine): Boolean
        fun selectTimeAndCallAlarm(time: LocalTime, activity: Activity)
        fun addOrRemoveFromFavourites(checkBox: CheckBox, busLineId: Long)
        fun getNextDeparture(
            busLine: Model.BusLine,
            startingPointIsFirstListed: Int
        ): Model.Departure?

        fun getNextDepartureFromRootText(busLineName: String, departureTime: String): String
        fun getNextDepartureFromDestText(busLineName: String, departureTime: String): String
    }

}