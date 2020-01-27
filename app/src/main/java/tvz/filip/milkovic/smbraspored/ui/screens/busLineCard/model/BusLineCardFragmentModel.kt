package tvz.filip.milkovic.smbraspored.ui.screens.busLineCard.model

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.provider.AlarmClock
import android.widget.CheckBox
import android.widget.TimePicker
import com.raizlabs.android.dbflow.kotlinextensions.delete
import com.raizlabs.android.dbflow.kotlinextensions.save
import com.raizlabs.android.dbflow.sql.language.Select
import tvz.filip.milkovic.smbraspored.R
import tvz.filip.milkovic.smbraspored.shared.model.FavBusLine_Table
import tvz.filip.milkovic.smbraspored.shared.model.Model
import tvz.filip.milkovic.smbraspored.shared.model.service.impl.ModelServiceImpl
import tvz.filip.milkovic.smbraspored.ui.screens.busLineCard.contract.BusLineCardContract
import java.time.LocalTime

class BusLineCardFragmentModel : BusLineCardContract.BusLineCardModel {

    private val delimiter = "-"
    private val semicolonAndSpace = ": "

    override fun checkIfBusLineIsInTheFavourites(busLine: Model.BusLine): Boolean {
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

    override fun selectTimeAndCallAlarm(time: LocalTime, activity: Activity) {
        val timehalfHour = time.minusMinutes(30)
        val mTimePicker = TimePickerDialog(
            activity,
            TimePickerDialog.OnTimeSetListener { _: TimePicker, hour: Int, min: Int ->
                val intent =
                    prepareAlarmIntent(activity.getString(R.string.text_alarm_message), hour, min)

                activity.startActivity(intent)
            },
            timehalfHour.hour,
            timehalfHour.minute,
            true
        )

        mTimePicker.setTitle(activity.getString(R.string.text_adjust_time))
        mTimePicker.show()
    }

    override fun addOrRemoveFromFavourites(checkBox: CheckBox, busLineId: Long) {
        val checked: Boolean = checkBox.isChecked

        when (checkBox.id) {
            R.id.addToFavourites -> {
                val favourite: Model.FavBusLine = Model.FavBusLine()
                favourite.idOfFavouriteBusLine = busLineId
                if (checked) {
                    favourite.save()
                } else {
                    favourite.delete()
                }
            }
        }
    }

    override fun getNextDeparture(
        busLine: Model.BusLine,
        startingPointIsFirstListed: Int
    ): Model.Departure? {
        return ModelServiceImpl.getNextDeparture(busLine, startingPointIsFirstListed)
    }

    override fun getNextDepartureFromRootText(busLineName: String, departureTime: String): String {
        val partsOfName = busLineName.split(delimiter, ignoreCase = true)
        return partsOfName.first().trim().plus(semicolonAndSpace).plus(departureTime.take(5))
    }

    override fun getNextDepartureFromDestText(busLineName: String, departureTime: String): String {
        val partsOfName = busLineName.split(delimiter, ignoreCase = true)
        return partsOfName.last().trim().plus(semicolonAndSpace).plus(departureTime.take(5))
    }

    private fun prepareAlarmIntent(msg: String, hour: Int, min: Int): Intent {
        val intent = Intent(AlarmClock.ACTION_SET_ALARM)
        intent.putExtra(AlarmClock.EXTRA_MESSAGE, msg)
        intent.putExtra(AlarmClock.EXTRA_HOUR, hour)
        intent.putExtra(AlarmClock.EXTRA_MINUTES, min)

        return intent
    }

}