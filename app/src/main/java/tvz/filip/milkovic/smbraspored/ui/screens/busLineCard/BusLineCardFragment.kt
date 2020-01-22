package tvz.filip.milkovic.smbraspored.ui.screens.busLineCard

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.raizlabs.android.dbflow.kotlinextensions.delete
import com.raizlabs.android.dbflow.kotlinextensions.save
import com.raizlabs.android.dbflow.sql.language.Select
import tvz.filip.milkovic.smbraspored.R
import tvz.filip.milkovic.smbraspored.shared.model.FavBusLine_Table
import tvz.filip.milkovic.smbraspored.shared.model.Model
import tvz.filip.milkovic.smbraspored.shared.model.service.impl.ModelServiceImpl
import java.time.LocalTime

private const val BUS_LINE = "busLine"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [BusLineCardFragment.OnBusLineCardFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [BusLineCardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BusLineCardFragment : Fragment() {
    var busLine: Model.BusLine? = null
    var favourite = false
    var nextDepartureFromRoot: Model.Departure? = null
    var nextDepartureFromDest: Model.Departure? = null

    private var listener: OnBusLineCardFragmentInteractionListener? = null
    private var unbinder: Unbinder? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            busLine = it.get(BUS_LINE) as Model.BusLine?
            favourite = checkIfBusLineIsInTheFavourites(busLine!!)
            nextDepartureFromRoot = ModelServiceImpl.getNextDeparture(busLine!!, 1)
            nextDepartureFromDest = ModelServiceImpl.getNextDeparture(busLine!!, 0)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_bus_line_card, container, false)
        unbinder = ButterKnife.bind(this, root!!)

        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnBusLineCardFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val text: TextView = view.findViewById(R.id.cardNameOfBusLine)
        text.text = busLine?.name

        val delimiter = "-"
        val partsOfName = busLine?.name?.split(delimiter, ignoreCase = true)

        val nextDepartRoot: TextView = view.findViewById(R.id.nextDepartureFromRoot)
        nextDepartRoot.text = nextDepartureFromRoot?.departureTime?.take(5)

        val nextDepartDest: TextView = view.findViewById(R.id.nextDepartureFromDest)
        nextDepartDest.text = nextDepartureFromDest?.departureTime?.take(5)

        val textFromRoot: TextView = view.findViewById(R.id.textView3)
        textFromRoot.text =
            getString(R.string.next_departure_text).plus(partsOfName?.first()?.trim())

        val textFromDest: TextView = view.findViewById(R.id.textView2)
        textFromDest.text =
            getString(R.string.next_departure_text).plus(partsOfName?.last()?.trim())


        val checkBox: CheckBox = view.findViewById(R.id.addToFavourites) as CheckBox
        checkBox.isChecked = favourite
        checkBox.setOnClickListener {
            onCheckBoxClicked(it)
        }
    }

    private fun checkIfBusLineIsInTheFavourites(busLine: Model.BusLine): Boolean {
        var fav: Boolean

        try {
            val result = Select()
                .from(Model.FavBusLine::class.java)
                .where(FavBusLine_Table.idOfFavouriteBusLine.eq(busLine.id))
                .querySingle()

            fav = result != null
        } catch (e: Exception) {
            fav = false
        }

        return fav
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    @OnClick(R.id.cardNameOfBusLine)
    fun onCheckBoxClicked(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.addToFavourites -> {
                    val favourite: Model.FavBusLine = Model.FavBusLine()
                    favourite.idOfFavouriteBusLine = busLine!!.id
                    if (checked) {
                        favourite.save()
                    } else {
                        favourite.delete()
                    }
                }
            }
        }
    }

    @OnClick(R.id.imageButton)
    internal fun onSetAlarmFromRootButtonCalled() {
        selectTimeAndCallAlarm(nextDepartureFromRoot!!.getDepartureTimeInLocalTime())
    }

    @OnClick(R.id.imageButton2)
    internal fun onSetAlarmFromDestButtonCalled() {
        selectTimeAndCallAlarm(nextDepartureFromDest!!.getDepartureTimeInLocalTime())
    }

    private fun selectTimeAndCallAlarm(time: LocalTime) {
        val timehalfHour = time.minusMinutes(30)
        val mTimePicker = TimePickerDialog(
            activity,
            TimePickerDialog.OnTimeSetListener { _: TimePicker, hour: Int, min: Int ->
                val intent = Intent(AlarmClock.ACTION_SET_ALARM)
                intent.putExtra(AlarmClock.EXTRA_MESSAGE, getString(R.string.text_alarm_message))
                intent.putExtra(AlarmClock.EXTRA_HOUR, hour)
                intent.putExtra(AlarmClock.EXTRA_MINUTES, min)

                startActivity(intent)
            },
            timehalfHour.hour,
            timehalfHour.minute,
            true
        )

        mTimePicker.setTitle(getString(R.string.text_adjust_time))
        mTimePicker.show()
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnBusLineCardFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
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
