package tvz.filip.milkovic.smbraspored.ui.screens.main.model

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.raizlabs.android.dbflow.kotlinextensions.save
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import tvz.filip.milkovic.smbraspored.R
import tvz.filip.milkovic.smbraspored.ui.screens.main.contract.MainContractInterface.MainModel
import tvz.filip.milkovic.smbraspored.web.service.SmbAppServiceInterface

class MainActivityModel : MainModel {

    private var appBarItems: Set<Int> = setOf(
        R.id.nav_home,
        R.id.nav_busLineList,
        R.id.nav_gallery,
        R.id.settings
    )

    private var disposable: Disposable? = null
    private val TAG = "MainActivityViewPresenter"

    private val smbAppServiceServe by lazy {
        SmbAppServiceInterface.create()
    }

    override fun getBusLinesFromWebService() {
        disposable = smbAppServiceServe.getAllBusLines()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    run {
                        result.forEach {
                            it.save()
                            it.departures!!.forEach { depart ->
                                depart.save()
                            }
                        }
                    }
                },
                { error ->
                    Log.e(TAG, "That ain't it chief $error.message")
                    // TODO when app has no connection to the server -> do something smart
                }
            )
    }

    override fun disposeOfDisposable() {
        disposable?.dispose()
    }

    override fun changeTheme(ctx: Context) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx)
        when (sharedPreferences.getString("theme_preference", "")) {
            ctx.getString(R.string.theme_name_automatic) -> {
                setThemeAndNightMode(
                    ctx,
                    R.style.AppThemeMain,
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                )
            }
            ctx.getString(R.string.theme_name_dark) -> {
                setThemeAndNightMode(ctx, R.style.AppThemeDark, AppCompatDelegate.MODE_NIGHT_YES)
            }
            ctx.getString(R.string.theme_name_light) -> {
                setThemeAndNightMode(ctx, R.style.AppThemeLight, AppCompatDelegate.MODE_NIGHT_NO)
            }
            ctx.getString(R.string.theme_name_holo) -> ctx.setTheme(R.style.AppThemeHolo)
        }
    }

    override fun getNavBarConfigurationItems(): Set<Int> {
        return appBarItems
    }

    private fun setThemeAndNightMode(ctx: Context, theme: Int, nightMode: Int) {
        ctx.setTheme(theme)
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }


}