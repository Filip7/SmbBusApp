package tvz.filip.milkovic.smbraspored.ui.screens.main.presenter

import tvz.filip.milkovic.smbraspored.ui.screens.main.contract.MainContractInterface.*
import tvz.filip.milkovic.smbraspored.ui.screens.main.model.MainActivityModel

class MainActivityPresenter(_view: MainView) :
    MainPresenter {

    private var view: MainView = _view
    private var model: MainModel = MainActivityModel()

    init {
        view.initView()
    }

    override fun initFloatingActionButton() {
        return model.createFloatingActionButton()
    }

}