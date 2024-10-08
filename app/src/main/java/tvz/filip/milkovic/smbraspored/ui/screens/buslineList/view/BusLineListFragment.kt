package tvz.filip.milkovic.smbraspored.ui.screens.buslineList.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tvz.filip.milkovic.smbraspored.R
import tvz.filip.milkovic.smbraspored.shared.model.Model
import tvz.filip.milkovic.smbraspored.ui.screens.buslineList.ModelBusLineRecyclerViewAdapter
import tvz.filip.milkovic.smbraspored.ui.screens.buslineList.contract.ContractBusLineListInterface
import tvz.filip.milkovic.smbraspored.ui.screens.buslineList.presenter.BusLineListFragmentPresenter

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [BusLineListFragment.OnBusLineListFragmentInteractionListener] interface.
 */
class BusLineListFragment : Fragment(), ContractBusLineListInterface.BusLineListView {

    private var columnCount = 1

    private var listener: OnListFragmentInteractionListener? = null

    private var localBusLinesList: MutableList<Model.BusLine> = ArrayList()

    private var presenter: ContractBusLineListInterface.BusLineListPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = BusLineListFragmentPresenter(this)

        localBusLinesList = presenter!!.getAllBusLinesAndDepartures()

        arguments?.let {
            columnCount = localBusLinesList.size
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_modelbusline_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter =
                    ModelBusLineRecyclerViewAdapter(
                        localBusLinesList,
                        listener
                    )
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Model.BusLine?, view: View)
    }

}
