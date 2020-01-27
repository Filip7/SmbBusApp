package tvz.filip.milkovic.smbraspored.ui.screens.home.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import tvz.filip.milkovic.smbraspored.R
import tvz.filip.milkovic.smbraspored.shared.model.Model
import tvz.filip.milkovic.smbraspored.ui.screens.home.ModelBusLineFavouritesRecyclerViewAdapter
import tvz.filip.milkovic.smbraspored.ui.screens.home.contract.HomeContractInterface
import tvz.filip.milkovic.smbraspored.ui.screens.home.presenter.HomeFragmentPresenter


class HomeFragment : Fragment(), HomeContractInterface.HomeView {

    private var columnCount = 1

    private var presenter: HomeContractInterface.HomePresenter? = null

    private var listener: OnFavouriteBusLineListFragmentInteractionListener? = null

    private var favBusLinesList: MutableList<Model.BusLine> = ArrayList()

    @BindView(R.id.favouritesList)
    lateinit var recView: RecyclerView

    @BindView(R.id.text_home)
    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = HomeFragmentPresenter(this)

        val favBuslinesIds = presenter?.getFavouriteBusLines()

        favBusLinesList = presenter!!.getBusLinesFromFavBusLines(favBuslinesIds!!)

        arguments?.let {
            columnCount = favBusLinesList.size
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savefdInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        ButterKnife.bind(this, root)

        if (favBusLinesList.isNotEmpty()) {
            textView.visibility = View.GONE
        } else {
            textView.visibility = View.VISIBLE
            textView.text = root.context.getString(R.string.text_no_favourite_busline)
        }

        // Set the adapter
        with(recView) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter =
                ModelBusLineFavouritesRecyclerViewAdapter(
                    favBusLinesList,
                    listener
                )
        }

        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFavouriteBusLineListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnListFragmentInteractionListener")
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