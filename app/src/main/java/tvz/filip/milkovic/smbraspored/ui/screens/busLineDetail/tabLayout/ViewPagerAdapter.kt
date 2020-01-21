package tvz.filip.milkovic.smbraspored.ui.screens.busLineDetail.tabLayout

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import tvz.filip.milkovic.smbraspored.shared.model.Model
import tvz.filip.milkovic.smbraspored.shared.model.TypeOfDay

private val TAB_TITLES = arrayOf(
    "Weekday",
    "Saturday",
    "Sunday"
)

class ViewPagerAdapter internal constructor(private val context: Context, fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var busLine: Model.BusLine? = null

    override fun getItem(position: Int): Fragment {
        val departures: List<Model.Departure> = busLine!!.departures!!.filter {
            it.typeOfDay == TypeOfDay.values()[position]
        }.toList()

        val dep = ArrayList<Model.Departure>(departures)
        return TabBusLineTimesheetFragment.newInstance(position + 1, busLine!!, dep)
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return TAB_TITLES[position]
    }

}