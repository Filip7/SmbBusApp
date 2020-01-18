package tvz.filip.milkovic.smbraspored.ui.screens.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import tvz.filip.milkovic.smbraspored.R
import tvz.filip.milkovic.smbraspored.web.service.SmbAppServiceInterface


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var unbinder: Unbinder? = null

    private val smbAppServiceServe by lazy {
        SmbAppServiceInterface.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savefdInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(this, Observer {
            textView.text = it
        })

        unbinder = ButterKnife.bind(this, root!!)

        return root
    }

    override fun onPause() {
        super.onPause()
        unbinder!!.unbind()
    }

    @OnClick(R.id.button)
    internal fun test() {
        Toast.makeText(activity, "Yes", Toast.LENGTH_SHORT).show()
        val response = smbAppServiceServe.getBusLinePDFForCode("147")
            .enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    println("Something does not work")
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val buffer = response.body()?.byteStream()
                    if (buffer != null) {
                        val file = context
                    }
                    println("Nes radi I guess")
                }

            }

            )
    }


    private fun writeResponseBodyToDisk(body: ResponseBody) {
        TODO("Implement this thing")
        /*   try {
               val futureStudioIconFile =
                   File(getExternalFilesDir(null) + File.separator.toString() + "Future Studio Icon.png")

               val inputStream: InputStream? = null
               val outputStream: OutputStream? = null
           } catch (e: Exception) {
           } finally {
           }*/
    }
}