package tvz.filip.milkovic.smbraspored.ui.screens.busLineCard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import kotlinx.android.synthetic.main.fragment_bus_line_card.view.*
import tvz.filip.milkovic.smbraspored.R
import tvz.filip.milkovic.smbraspored.shared.model.Model
import tvz.filip.milkovic.smbraspored.ui.screens.busLineCard.contract.BusLineCardContract
import tvz.filip.milkovic.smbraspored.ui.screens.busLineCard.presenter.BusLineCardFragmentPresenter

private const val BUS_LINE = "busLine"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [BusLineCardFragment.OnBusLineCardFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [BusLineCardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BusLineCardFragment : Fragment(), BusLineCardContract.BusLineCardView {

    var busLine: Model.BusLine? = null

    var favourite = false

    var nextDepartureFromRoot: Model.Departure? = null

    var nextDepartureFromDest: Model.Departure? = null

    private var presenter: BusLineCardContract.BusLineCardPresenter? = null

    private var unbinder: Unbinder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = BusLineCardFragmentPresenter(this)

        arguments?.let {
            busLine = it.get(BUS_LINE) as Model.BusLine?
            favourite = presenter!!.checkIfBusLineIsInTheFavourites(busLine!!)
            nextDepartureFromRoot = presenter?.getNextDeparture(busLine!!, 1)
            nextDepartureFromDest = presenter?.getNextDeparture(busLine!!, 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_bus_line_card, container, false)
        unbinder = ButterKnife.bind(this, root)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val text: TextView = view.findViewById(R.id.cardNameOfBusLine)
        text.text = busLine?.name

        view.nextDepartureFromRoot.text = presenter?.getNextDepartureFromRootText(
            busLine!!.name,
            nextDepartureFromRoot!!.departureTime
        )

        view.nextDepartureFromDest.text = presenter?.getNextDepartureFromDestText(
            busLine!!.name,
            nextDepartureFromDest!!.departureTime
        )

        view.addToFavourites.isChecked = favourite
        view.addToFavourites.setOnClickListener {
            onCheckBoxClicked(it)
        }
    }

    @OnClick(R.id.cardNameOfBusLine)
    fun onCheckBoxClicked(view: View) {
        if (view is CheckBox) {
            presenter?.addOrRemoveFromFavourites(view, busLine!!.id)
        }
    }

    @OnClick(R.id.alarm_departure_root)
    internal fun onSetAlarmFromRootButtonCalled() {
        presenter!!.selectTimeAndCallAlarm(
            nextDepartureFromRoot!!.getDepartureTimeInLocalTime(),
            activity!!
        )
    }

    @OnClick(R.id.alarm_departure_dest)
    internal fun onSetAlarmFromDestButtonCalled() {
        presenter!!.selectTimeAndCallAlarm(
            nextDepartureFromDest!!.getDepartureTimeInLocalTime(),
            activity!!
        )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param busLine Parameter 1.
         * @return A new instance of fragment BusLineCardFragment.
         */
        @JvmStatic
        fun newInstance(busLine: Model.BusLine) =
            BusLineCardFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BUS_LINE, busLine)
                }
            }
    }
}
