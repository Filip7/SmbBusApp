package tvz.filip.milkovic.smbraspored.ui.screens.home.contract

import tvz.filip.milkovic.smbraspored.shared.model.Model

interface HomeContractInterface {

    interface HomeView

    interface HomePresenter {
        fun getFavouriteBusLines(): List<Model.FavBusLine>
        fun getBusLinesFromFavBusLines(busLineFavList: List<Model.FavBusLine>): MutableList<Model.BusLine>
    }

    interface HomeModel {
        fun getFavouriteBusLines(): List<Model.FavBusLine>
        fun getBusLinesFromFavBusLines(busLineFavList: List<Model.FavBusLine>): MutableList<Model.BusLine>
    }

}