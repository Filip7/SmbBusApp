package tvz.filip.milkovic.smbraspored.ui.screens.busLineCard.presenter

import android.app.Activity
import android.widget.CheckBox
import tvz.filip.milkovic.smbraspored.shared.model.Model
import tvz.filip.milkovic.smbraspored.ui.screens.busLineCard.contract.BusLineCardContract
import tvz.filip.milkovic.smbraspored.ui.screens.busLineCard.model.BusLineCardFragmentModel
import java.time.LocalTime

class BusLineCardFragmentPresenter(_view: BusLineCardContract.BusLineCardView) :
    BusLineCardContract.BusLineCardPresenter {

    private var view: BusLineCardContract.BusLineCardView = _view
    private var model: BusLineCardContract.BusLineCardModel = BusLineCardFragmentModel()

    override fun checkIfBusLineIsInTheFavourites(busLine: Model.BusLine): Boolean {
        return model.checkIfBusLineIsInTheFavourites(busLine)
    }

    override fun selectTimeAndCallAlarm(time: LocalTime, activity: Activity) {
        model.selectTimeAndCallAlarm(time, activity)
    }

    override fun addOrRemoveFromFavourites(checkBox: CheckBox, busLineId: Long) {
        model.addOrRemoveFromFavourites(checkBox, busLineId)
    }

    override fun getNextDeparture(
        busLine: Model.BusLine,
        startingPointIsFirstListed: Int
    ): Model.Departure? {
        return model.getNextDeparture(busLine, startingPointIsFirstListed)
    }

    override fun getNextDepartureFromRootText(busLineName: String, departureTime: String): String {
        return model.getNextDepartureFromRootText(busLineName, departureTime)
    }

    override fun getNextDepartureFromDestText(busLineName: String, departureTime: String): String {
        return model.getNextDepartureFromDestText(busLineName, departureTime)
    }

}