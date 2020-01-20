package tvz.filip.milkovic.smbraspored.ui.screens.busLineDetail

import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import kotlinx.android.synthetic.main.bus_line_detail_fragment.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import tvz.filip.milkovic.smbraspored.BuildConfig
import tvz.filip.milkovic.smbraspored.R
import tvz.filip.milkovic.smbraspored.shared.model.Model
import tvz.filip.milkovic.smbraspored.ui.screens.busLineCard.BusLineCardFragment
import tvz.filip.milkovic.smbraspored.web.service.SmbAppServiceInterface
import java.io.File
import java.io.FileOutputStream
import java.util.*


class BusLineDetailFragment : Fragment() {

    companion object {
        fun newInstance() =
            BusLineDetailFragment()
    }

    private val smbAppServiceServe by lazy {
        SmbAppServiceInterface.create()
    }

    private var unbinder: Unbinder? = null

    private lateinit var viewModel: BusLineDetailViewModel

    private val args: BusLineDetailFragmentArgs by navArgs()

    private var busLine: Model.BusLine? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.bus_line_detail_fragment, container, false)
        unbinder = ButterKnife.bind(this, root!!)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(BusLineDetailViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        busLine = args.selectedBusLine

        var card = BusLineCardFragment.newInstance(busLine!!)
        fragmentManager!!.beginTransaction().apply {
            replace(R.id.busLineCard, card)
            commit()
        }

        itemsswipetorefresh.setOnRefreshListener {
            card = BusLineCardFragment.newInstance(busLine!!)
            fragmentManager!!.beginTransaction().apply {
                replace(R.id.busLineCard, card)
                commit()
            }

            itemsswipetorefresh.isRefreshing = false
        }

        getBusLinePdf(busLine!!.code)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder!!.unbind()
    }

    fun getBusLinePdf(code: String) {
        val file =
            File(context!!.getExternalFilesDir(null).toString() + "/" + code + ".pdf")

        if (!file.exists()) {
            smbAppServiceServe.getBusLinePDFForCode(code)
                .enqueue(object : retrofit2.Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.e("", "Cannot download PDF file " + busLine?.code + ".pdf")
                    }

                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            saveFileToDisk(response.body()!!, code)
                        }
                    }
                }
                )
        }
    }

    @OnClick(R.id.button2)
    internal fun onPdfDownloadButtonClicked() {
        val pdfFile =
            File(context!!.getExternalFilesDir(null).toString() + "/" + busLine?.code + ".pdf")
        val contentUri: Uri = FileProvider.getUriForFile(
            context!!,
            "${BuildConfig.APPLICATION_ID}.provider",
            pdfFile
        )
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(contentUri, "application/pdf")
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pm = activity!!.packageManager
        if (intent.resolveActivity(pm) != null) {
            startActivity(intent)
        } else {
            Log.e("", "Cannot start activity to open PDF file " + busLine?.code + ".pdf")
        }
    }

    @OnClick(R.id.button)
    internal fun onSetAlarmButtonCalled() {
        selectTimeAndCallAlarm()
    }

    private fun selectTimeAndCallAlarm() {
        val mCurrentTime = Calendar.getInstance()

        val hour = mCurrentTime.get(Calendar.HOUR_OF_DAY)
        val min = mCurrentTime.get(Calendar.HOUR_OF_DAY)
        val mTimePicker = TimePickerDialog(
            activity,
            TimePickerDialog.OnTimeSetListener { _: TimePicker, hour: Int, min: Int ->
                val intent = Intent(AlarmClock.ACTION_SET_ALARM)
                intent.putExtra(AlarmClock.EXTRA_MESSAGE, "New Alarm")
                intent.putExtra(AlarmClock.EXTRA_HOUR, hour)
                intent.putExtra(AlarmClock.EXTRA_MINUTES, min)

                startActivity(intent)
            },
            hour,
            min,
            true
        )

        mTimePicker.setTitle("Adjust time")
        mTimePicker.show()
    }

    private fun saveFileToDisk(body: ResponseBody, code: String) {
        try {
            val r = Runnable {
                val file =
                    File(context!!.getExternalFilesDir(null).toString() + "/" + code + ".pdf")
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

}
