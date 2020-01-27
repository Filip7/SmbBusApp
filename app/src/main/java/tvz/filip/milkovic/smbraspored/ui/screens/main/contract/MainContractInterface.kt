package tvz.filip.milkovic.smbraspored.ui.screens.main.contract

import android.content.Context

interface MainContractInterface {

    interface MainView

    interface MainPresenter {
        fun fetchBusLines()
        fun disposeOfDisposable()
        fun changeTheme(context: Context)
        fun getNavBarConfigurationItems(): Set<Int>
    }

    interface MainModel {
        fun getBusLinesFromWebService()
        fun disposeOfDisposable()
        fun changeTheme(context: Context)
        fun getNavBarConfigurationItems(): Set<Int>
    }
}