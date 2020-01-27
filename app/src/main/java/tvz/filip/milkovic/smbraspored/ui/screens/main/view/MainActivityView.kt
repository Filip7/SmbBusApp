package tvz.filip.milkovic.smbraspored.ui.screens.main.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import butterknife.BindView
import butterknife.ButterKnife
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.material.navigation.NavigationView
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager
import tvz.filip.milkovic.smbraspored.R
import tvz.filip.milkovic.smbraspored.shared.model.Model
import tvz.filip.milkovic.smbraspored.ui.screens.buslineList.view.BusLineListFragment
import tvz.filip.milkovic.smbraspored.ui.screens.buslineList.view.BusLineListFragmentDirections
import tvz.filip.milkovic.smbraspored.ui.screens.home.view.HomeFragment
import tvz.filip.milkovic.smbraspored.ui.screens.home.view.HomeFragmentDirections
import tvz.filip.milkovic.smbraspored.ui.screens.main.contract.MainContractInterface.MainPresenter
import tvz.filip.milkovic.smbraspored.ui.screens.main.contract.MainContractInterface.MainView
import tvz.filip.milkovic.smbraspored.ui.screens.main.presenter.MainActivityPresenter
import tvz.filip.milkovic.smbraspored.ui.screens.settings.SettingsActivity

class MainActivityView : AppCompatActivity(), MainView,
    BusLineListFragment.OnListFragmentInteractionListener,
    HomeFragment.OnFavouriteBusLineListFragmentInteractionListener {

    private lateinit var appBarConfiguration: AppBarConfiguration

    private var presenter: MainPresenter? = null

    @BindView(R.id.drawer_layout)
    lateinit var drawerLayout: DrawerLayout

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.nav_view)
    lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        presenter = MainActivityPresenter(this)

        presenter?.fetchBusLines()

        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration =
            AppBarConfiguration(presenter!!.getNavBarConfigurationItems(), drawerLayout)
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

        presenter?.changeTheme(this)
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
        presenter?.disposeOfDisposable()
    }

    override fun onListFragmentInteraction(item: Model.BusLine?, view: View) {
        val action = BusLineListFragmentDirections.actionNavBusLineListToBusLineDetailFragment(item)
        view.findNavController().navigate(action)
    }

    override fun onFavouriteListFragmentInteraction(item: Model.BusLine?, view: View) {
        val action = HomeFragmentDirections.actionNavHomeToBusLineDetailFragment(item)
        view.findNavController().navigate(action)
    }

}
