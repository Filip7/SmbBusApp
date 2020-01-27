package tvz.filip.milkovic.smbraspored.ui.screens.home.presenter

import tvz.filip.milkovic.smbraspored.shared.model.Model
import tvz.filip.milkovic.smbraspored.ui.screens.home.contract.HomeContractInterface
import tvz.filip.milkovic.smbraspored.ui.screens.home.model.HomeFragmentViewModel
import tvz.filip.milkovic.smbraspored.ui.screens.home.view.HomeFragment

class HomeFragmentPresenter(_view: HomeFragment) : HomeContractInterface.HomePresenter {

    private var view: HomeContractInterface.HomeView = _view
    private var model: HomeContractInterface.HomeModel = HomeFragmentViewModel()

    override fun getFavouriteBusLines(): List<Model.FavBusLine> {
        return model.getFavouriteBusLines()
    }

    override fun getBusLinesFromFavBusLines(busLineFavList: List<Model.FavBusLine>): MutableList<Model.BusLine> {
        return model.getBusLinesFromFavBusLines(busLineFavList)
    }

}