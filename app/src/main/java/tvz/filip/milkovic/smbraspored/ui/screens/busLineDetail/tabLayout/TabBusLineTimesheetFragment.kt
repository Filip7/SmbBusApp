package tvz.filip.milkovic.smbraspored.ui.screens.busLineDetail.tabLayout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import tvz.filip.milkovic.smbraspored.R
import tvz.filip.milkovic.smbraspored.shared.model.Model

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TabBusLineTimesheetFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TabBusLineTimesheetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TabBusLineTimesheetFragment : Fragment() {

    private lateinit var pageViewModel: TabBusLineTimeSheetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel =
            ViewModelProviders.of(this).get(TabBusLineTimeSheetViewModel::class.java).apply {
                setIndex(arguments?.getInt(ARG_NUMBER_OF_TAB) ?: 1)
                setBusLine(arguments?.getParcelable<Model.BusLine>(ARG_BUS_LINE) as Model.BusLine)
                setDepartures(arguments?.getParcelableArrayList<Model.Departure>(ARG_DEPARTURES) as List<Model.Departure>)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_tab_bus_line_timesheet, container, false)
        val text1: TextView = root.findViewById(R.id.details)
        val text4: TextView = root.findViewById(R.id.textView4)
        val text5: TextView = root.findViewById(R.id.textView5)
        val text6: TextView = root.findViewById(R.id.textView6)

        pageViewModel.busLine.observe(this, Observer<Model.BusLine> {
            val delimiter = "-"
            val partsOfName = it.name.split(delimiter, ignoreCase = true)

            text4.text = partsOfName.first().trim()
        })

        pageViewModel.busLine.observe(this, Observer<Model.BusLine> {
            val delimiter = "-"
            val partsOfName = it.name.split(delimiter, ignoreCase = true)

            text6.text = partsOfName.last().trim()
        })


        pageViewModel.departures.observe(this, Observer<List<Model.Departure>> {
            text1.text = it.filter { it.startingPointIsFirstListed == 1 }
                .joinToString(", ") { departure -> departure.departureTime.take(5) }
        })

        pageViewModel.departures.observe(this, Observer<List<Model.Departure>> {
            text5.text = it.filter { it.startingPointIsFirstListed == 0 }
                .joinToString(", ") { departure -> departure.departureTime.take(5) }
        })

        return root
    }

    companion object {

        private const val ARG_NUMBER_OF_TAB = "arg_tab_number"
        private const val ARG_BUS_LINE = "arg_bus_line"
        private const val ARG_DEPARTURES = "arg_departures"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 BusLine
         * @return A new instance of fragment TabBusLineTimesheetFragment.
         */
        @JvmStatic
        fun newInstance(param1: Int, param2: Model.BusLine, param3: ArrayList<Model.Departure>) =
            TabBusLineTimesheetFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_NUMBER_OF_TAB, param1)
                    putParcelable(ARG_BUS_LINE, param2)
                    putParcelableArrayList(ARG_DEPARTURES, param3)
                }
            }
    }
}
