package tvz.filip.milkovic.smbraspored.ui.screens.main.contract

interface MainContractInterface {

    interface MainView {
        fun initView()
    }

    interface MainPresenter {
        fun initFloatingActionButton()
    }

    interface MainModel {
        fun createFloatingActionButton()
    }
}