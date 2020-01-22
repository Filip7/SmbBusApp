package tvz.filip.milkovic.smbraspored.ui.screens.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.list
import com.raizlabs.android.dbflow.kotlinextensions.select
import tvz.filip.milkovic.smbraspored.R
import tvz.filip.milkovic.smbraspored.shared.model.BusLine_Table
import tvz.filip.milkovic.smbraspored.shared.model.Departure_Table
import tvz.filip.milkovic.smbraspored.shared.model.Model


class HomeFragment : Fragment() {

    private var columnCount = 1

    private lateinit var homeViewModel: HomeViewModel

    private var listener: OnFavouriteBusLineListFragmentInteractionListener? = null

    private var localBusLinesList: MutableList<Model.BusLine> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val favBuslinesIds = (select.from((Model.FavBusLine::class))).list
        favBuslinesIds.forEach {
            (select.from(Model.BusLine::class).where(BusLine_Table.id.eq(it.idOfFavouriteBusLine))).list
                .forEach {
                    localBusLinesList.add(it)
                }
        }

        localBusLinesList.forEach {
            it.departures =
                (select.from(Model.Departure::class).where(Departure_Table.busLineId.eq(it.id))).list
        }
        localBusLinesList.sortBy { busLine -> busLine.code.toInt() }

        arguments?.let {
            columnCount = localBusLinesList.size
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savefdInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val recView: View = root.findViewById(R.id.favouritesList)
        val textView: TextView = root.findViewById(R.id.text_home)

        if (localBusLinesList.isNotEmpty()) {
            textView.visibility = View.GONE
        } else {

            textView.visibility = View.VISIBLE
            textView.text = root.context.getString(R.string.text_no_favourite_busline)
        }

        // Set the adapter
        if (recView is RecyclerView) {
            with(recView) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter =
                    ModelBusLineFavouritesRecyclerViewAdapter(
                        localBusLinesList,
                        listener
                    )
            }
        }

        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFavouriteBusLineListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener") as Throwable
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFavouriteBusLineListFragmentInteractionListener {
        fun onFavouriteListFragmentInteraction(item: Model.BusLine?, view: View)
    }

}