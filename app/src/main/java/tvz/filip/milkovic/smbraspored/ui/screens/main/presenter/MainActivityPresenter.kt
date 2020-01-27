package tvz.filip.milkovic.smbraspored.ui.screens.main.presenter

import android.content.Context
import tvz.filip.milkovic.smbraspored.ui.screens.main.contract.MainContractInterface.*
import tvz.filip.milkovic.smbraspored.ui.screens.main.model.MainActivityModel

class MainActivityPresenter(_view: MainView) :
    MainPresenter {

    private var view: MainView = _view
    private var model: MainModel = MainActivityModel()

    override fun fetchBusLines() {
        model.getBusLinesFromWebService()
    }

    override fun disposeOfDisposable() {
        model.disposeOfDisposable()
    }

    override fun changeTheme(context: Context) {
        model.changeTheme(context)
    }

    override fun getNavBarConfigurationItems(): Set<Int> {
        return model.getNavBarConfigurationItems()
    }


}