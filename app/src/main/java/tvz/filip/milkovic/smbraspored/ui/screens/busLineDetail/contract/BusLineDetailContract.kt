package tvz.filip.milkovic.smbraspored.ui.screens.busLineDetail.contract

import android.app.Activity
import android.content.Context

interface BusLineDetailContract {

    interface BusLineDetailView

    interface BusLineDetailPresenter {
        fun downloadBusLinePdf(code: String, ctx: Context)
        fun openPdfDocument(code: String, ctx: Context, activity: Activity)
        fun getTitleWithBusLineCode(toolbarTitle: String, busLineCode: String): String
    }

    interface BusLineDetailModel {
        fun downloadBusLinePdf(code: String, ctx: Context)
        fun openPdfDocument(code: String, ctx: Context, activity: Activity)
        fun getTitleWithBusLineCode(toolbarTitle: String, busLineCode: String): String
    }

}