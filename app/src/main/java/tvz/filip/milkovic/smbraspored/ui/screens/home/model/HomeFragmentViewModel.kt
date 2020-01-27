package tvz.filip.milkovic.smbraspored.ui.screens.home.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.list
import com.raizlabs.android.dbflow.kotlinextensions.select
import tvz.filip.milkovic.smbraspored.shared.model.BusLine_Table
import tvz.filip.milkovic.smbraspored.shared.model.Departure_Table
import tvz.filip.milkovic.smbraspored.shared.model.Model
import tvz.filip.milkovic.smbraspored.ui.screens.home.contract.HomeContractInterface

class HomeFragmentViewModel : ViewModel(), HomeContractInterface.HomeModel {

    private val _text = MutableLiveData<String>().apply {
        value = "You have no favourite bus lines!"
    }

    val text: LiveData<String> = _text

    private var favBusLinesList: MutableList<Model.BusLine> = ArrayList()

    override fun getFavouriteBusLines(): List<Model.FavBusLine> {
        return (select.from((Model.FavBusLine::class))).list
    }

    override fun getBusLinesFromFavBusLines(busLineFavList: List<Model.FavBusLine>): MutableList<Model.BusLine> {
        busLineFavList.forEach {
            getBusLinesFromFavouriteId(it.idOfFavouriteBusLine)
                .forEach {
                    favBusLinesList.add(it)
                }
        }

        setDepartures()
        sortBusLinesByCode()

        return favBusLinesList
    }

    private fun sortBusLinesByCode() {
        favBusLinesList.sortBy { busLine -> busLine.code.toInt() }
    }

    private fun setDepartures() {
        favBusLinesList.forEach {
            it.departures = getDeparturesForBusLine(it.id)
        }
    }

    private fun getBusLinesFromFavouriteId(idOfFavouriteBusLine: Long): List<Model.BusLine> {
        return (select.from(Model.BusLine::class).where(BusLine_Table.id.eq(idOfFavouriteBusLine))).list
    }

    private fun getDeparturesForBusLine(id: Long): List<Model.Departure> {
        return (select.from(Model.Departure::class).where(Departure_Table.busLineId.eq(id))).list
    }


}
