package tvz.filip.milkovic.smbraspored.ui.screens.buslineList.presenter

import tvz.filip.milkovic.smbraspored.shared.model.Model
import tvz.filip.milkovic.smbraspored.ui.screens.buslineList.contract.ContractBusLineListInterface
import tvz.filip.milkovic.smbraspored.ui.screens.buslineList.model.BusLineListFragmentModel

class BusLineListFragmentPresenter(_view: ContractBusLineListInterface.BusLineListView) :
    ContractBusLineListInterface.BusLineListPresenter {

    private var view: ContractBusLineListInterface.BusLineListView = _view
    private var model: ContractBusLineListInterface.BusLineListModel = BusLineListFragmentModel()

    override fun getAllBusLinesAndDepartures(): MutableList<Model.BusLine> {
        return model.getAllBusLines()
    }
}