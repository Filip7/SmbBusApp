package tvz.filip.milkovic.smbraspored.web.service

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming
import tvz.filip.milkovic.smbraspored.shared.model.Model

interface SmbAppServiceInterface {

    @GET("/api/bus-line/all-bus-lines")
    fun getAllBusLines(): Observable<List<Model.BusLine>>

    @Streaming
    @GET("/api/bus-line/get-pdf/{code}")
    fun getBusLinePDFForCode(@Path("code") code: String): Call<ResponseBody>

    companion object {
        fun create(): SmbAppServiceInterface {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://10.0.2.2:8080")
                .build()

            return retrofit.create(SmbAppServiceInterface::class.java)
        }
    }
}