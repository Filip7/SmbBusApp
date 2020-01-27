package tvz.filip.milkovic.smbraspored.ui.screens.busLineDetail.presenter

import android.app.Activity
import android.content.Context
import tvz.filip.milkovic.smbraspored.ui.screens.busLineDetail.contract.BusLineDetailContract
import tvz.filip.milkovic.smbraspored.ui.screens.busLineDetail.model.BusLineDetailViewModel
import tvz.filip.milkovic.smbraspored.ui.screens.busLineDetail.view.BusLineDetailFragment

class BusLineDetailFragmentPresenter(_view: BusLineDetailFragment) :
    BusLineDetailContract.BusLineDetailPresenter {

    private var view: BusLineDetailContract.BusLineDetailView = _view
    private var model: BusLineDetailContract.BusLineDetailModel = BusLineDetailViewModel()

    override fun downloadBusLinePdf(code: String, ctx: Context) {
        model.downloadBusLinePdf(code, ctx)
    }

    override fun openPdfDocument(code: String, ctx: Context, activity: Activity) {
        model.openPdfDocument(code, ctx, activity)
    }

    override fun getTitleWithBusLineCode(toolbarTitle: String, busLineCode: String): String {
        return model.getTitleWithBusLineCode(toolbarTitle, busLineCode)
    }

}