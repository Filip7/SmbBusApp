package tvz.filip.milkovic.smbraspored.ui.screens.busLineDetail.tabLayout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import tvz.filip.milkovic.smbraspored.shared.model.Model

class TabBusLineTimeSheetViewModel : ViewModel() {

    private val _index = MutableLiveData<Int>()
    private val _busLine = MutableLiveData<Model.BusLine>()
    private val _departures = MutableLiveData<List<Model.Departure>>()

    val busLine: LiveData<Model.BusLine> = Transformations.map(_busLine) {
        it
    }

    val departures: LiveData<List<Model.Departure>> = Transformations.map(_departures) {
        it
    }

    val textId: LiveData<String> = Transformations.map(_index) {
        "$it"
    }

    val text: LiveData<String> = Transformations.map(_busLine) {
        "Hello world from section: ${it.name}"
    }

    fun setIndex(index: Int) {
        _index.value = index
    }

    fun setBusLine(busline: Model.BusLine) {
        _busLine.value = busline
    }

    fun setDepartures(departures: List<Model.Departure>) {
        _departures.value = departures
    }

}