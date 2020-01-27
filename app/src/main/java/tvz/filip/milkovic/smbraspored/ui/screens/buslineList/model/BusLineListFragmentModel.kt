package tvz.filip.milkovic.smbraspored.ui.screens.buslineList.model

import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.list
import com.raizlabs.android.dbflow.kotlinextensions.select
import tvz.filip.milkovic.smbraspored.shared.model.Departure_Table
import tvz.filip.milkovic.smbraspored.shared.model.Model
import tvz.filip.milkovic.smbraspored.ui.screens.buslineList.contract.ContractBusLineListInterface

class BusLineListFragmentModel : ContractBusLineListInterface.BusLineListModel {

    private var localBusLinesList: MutableList<Model.BusLine> = ArrayList()

    override fun getAllBusLines(): MutableList<Model.BusLine> {
        localBusLinesList = (select.from(Model.BusLine::class)).list

        setDeparturesOfBusLines()
        sortBusLinesByCode()

        return localBusLinesList
    }

    private fun sortBusLinesByCode() {
        localBusLinesList.sortBy { busLine -> busLine.code.toInt() }
    }

    private fun setDeparturesOfBusLines() {
        localBusLinesList.forEach {
            it.departures =
                (select.from(Model.Departure::class).where(Departure_Table.busLineId.eq(it.id))).list
        }
    }
}