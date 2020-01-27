package tvz.filip.milkovic.smbraspored.ui.screens.busLineDetail.model

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import tvz.filip.milkovic.smbraspored.BuildConfig
import tvz.filip.milkovic.smbraspored.ui.screens.busLineDetail.contract.BusLineDetailContract
import tvz.filip.milkovic.smbraspored.web.service.SmbAppServiceInterface
import java.io.File
import java.io.FileOutputStream

class BusLineDetailViewModel : ViewModel(), BusLineDetailContract.BusLineDetailModel {

    private val smbAppServiceServe by lazy {
        SmbAppServiceInterface.create()
    }

    private var TAG: String = "BusLineDetailFragment"

    override fun downloadBusLinePdf(code: String, ctx: Context) {
        val file =
            File(ctx.getExternalFilesDir(null).toString() + "/" + code + ".pdf")

        if (!file.exists()) {
            smbAppServiceServe.getBusLinePDFForCode(code)
                .enqueue(object : retrofit2.Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("", "Cannot download PDF file $code.pdf")
                    }

                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            saveFileToDisk(response.body()!!, code, ctx)
                        }
                    }
                }
                )
        }
    }

    override fun openPdfDocument(code: String, ctx: Context, activity: Activity) {
        val pdfFile =
            File(ctx.getExternalFilesDir(null).toString() + "/" + code + ".pdf")
        val contentUri: Uri = FileProvider.getUriForFile(
            ctx,
            "${BuildConfig.APPLICATION_ID}.provider",
            pdfFile
        )

        val intent = createActionViewIntent(contentUri)

        val pm = activity.packageManager
        if (intent.resolveActivity(pm) != null) {
            ctx.startActivity(intent)
        } else {
            Log.e(TAG, "Cannot start activity to open PDF file $code.pdf")
        }
    }

    override fun getTitleWithBusLineCode(toolbarTitle: String, busLineCode: String): String {
        return "$toolbarTitle $busLineCode"
    }

    private fun saveFileToDisk(body: ResponseBody, code: String, ctx: Context) {
        try {
            val r = Runnable {
                val file =
                    File(ctx.getExternalFilesDir(null).toString() + "/" + code + ".pdf")
                file.createNewFile()
                val outputStream = FileOutputStream(file)
                outputStream.write(body.bytes())
                outputStream.flush()
                outputStream.close()
            }

            val t = Thread(r)
            t.start()
        } catch (e: Exception) {
            throw e
        }
    }

    private fun createActionViewIntent(contentUri: Uri): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(contentUri, "application/pdf")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        return intent
    }

}
