package tvz.filip.milkovic.smbraspored.ui.screens.main.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.material.navigation.NavigationView
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.kotlinextensions.save
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import tvz.filip.milkovic.smbraspored.R
import tvz.filip.milkovic.smbraspored.shared.model.Model
import tvz.filip.milkovic.smbraspored.ui.screens.busLineCard.BusLineCardFragment
import tvz.filip.milkovic.smbraspored.ui.screens.buslineList.BusLineListFragment
import tvz.filip.milkovic.smbraspored.ui.screens.buslineList.BusLineListFragmentDirections
import tvz.filip.milkovic.smbraspored.ui.screens.home.HomeFragment
import tvz.filip.milkovic.smbraspored.ui.screens.home.HomeFragmentDirections
import tvz.filip.milkovic.smbraspored.ui.screens.main.contract.MainContractInterface.MainPresenter
import tvz.filip.milkovic.smbraspored.ui.screens.main.contract.MainContractInterface.MainView
import tvz.filip.milkovic.smbraspored.ui.screens.main.presenter.MainActivityPresenter
import tvz.filip.milkovic.smbraspored.ui.screens.settings.SettingsActivity
import tvz.filip.milkovic.smbraspored.web.service.SmbAppServiceInterface

class MainActivityView : AppCompatActivity(), MainView,
    BusLineListFragment.OnListFragmentInteractionListener,
    BusLineCardFragment.OnBusLineCardFragmentInteractionListener,
    HomeFragment.OnFavouriteBusLineListFragmentInteractionListener {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var presenter: MainPresenter? = null
    private val TAG = "MainActivityView"

    private val smbAppServiceServe by lazy {
        SmbAppServiceInterface.create()
    }

    var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainActivityPresenter(this)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_busLineList,
                R.id.nav_gallery,
                R.id.settings
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // DbFlow initialization
        FlowManager.init(
            FlowConfig.builder(this)
                .openDatabasesOnInit(true)
                .build()
        )

        // Fresco initialization
        Fresco.initialize(this)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        when (sharedPreferences.getString("theme_preference", "")) {
            getString(R.string.theme_name_automatic) -> {
                setTheme(R.style.AppThemeMain)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            getString(R.string.theme_name_dark) -> {
                setTheme(R.style.AppThemeDark)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            getString(R.string.theme_name_light) -> {
                setTheme(R.style.AppThemeLight)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            getString(R.string.theme_name_holo) -> setTheme(R.style.AppThemeHolo)
        }

        fetchAllBusLines()
    }

    override fun initView() {
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)

            return true
        }

        return false
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    override fun onListFragmentInteraction(item: Model.BusLine?, view: View) {
        val action = BusLineListFragmentDirections.actionNavBusLineListToBusLineDetailFragment(item)

        view.findNavController().navigate(action)
    }

    private fun fetchAllBusLines() {
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

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFavouriteListFragmentInteraction(item: Model.BusLine?, view: View) {
        val action = HomeFragmentDirections.actionNavHomeToBusLineDetailFragment(item)

        view.findNavController().navigate(action)
    }

}
