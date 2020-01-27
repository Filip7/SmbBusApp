package tvz.filip.milkovic.smbraspored.ui.screens.buslineList.contract

import tvz.filip.milkovic.smbraspored.shared.model.Model

interface ContractBusLineListInterface {

    interface BusLineListView

    interface BusLineListPresenter {
        fun getAllBusLinesAndDepartures(): MutableList<Model.BusLine>
    }

    interface BusLineListModel {
        fun getAllBusLines(): MutableList<Model.BusLine>
    }
}