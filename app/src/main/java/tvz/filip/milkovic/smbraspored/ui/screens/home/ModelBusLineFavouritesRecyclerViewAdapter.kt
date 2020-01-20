package tvz.filip.milkovic.smbraspored.ui.screens.home


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tvz.filip.milkovic.smbraspored.R
import tvz.filip.milkovic.smbraspored.shared.model.Model
import tvz.filip.milkovic.smbraspored.shared.model.service.impl.ModelServiceImpl

/**
 * [RecyclerView.Adapter] that can display a [Model.BusLine] and makes a call to the
 * specified [OnBusLineListFragmentInteractionListener].
 */
class ModelBusLineFavouritesRecyclerViewAdapter(
    private val mValues: List<Model.BusLine>,
    private val mListener: HomeFragment.OnFavouriteBusLineListFragmentInteractionListener?
) : RecyclerView.Adapter<ModelBusLineFavouritesRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Model.BusLine
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.

            mListener?.onFavouriteListFragmentInteraction(item, v)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_bus_line_card, parent, false)

        val checkBox: CheckBox = view.findViewById(R.id.addToFavourites)
        checkBox.visibility = View.GONE

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.addToFavouritesCheckBox.isChecked = true
        holder.cardNameOfBusLineTextVIew.text = item.name
        holder.nextDepartureFromRoot.text =
            ModelServiceImpl.getNextDeparture(item, 1)?.departureTime
        holder.nextDepartureFromDest.text =
            ModelServiceImpl.getNextDeparture(item, 0)?.departureTime

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val addToFavouritesCheckBox: CheckBox = mView.findViewById(R.id.addToFavourites)
        val cardNameOfBusLineTextVIew: TextView = mView.findViewById(R.id.cardNameOfBusLine)
        val nextDepartureFromRoot: TextView = mView.findViewById(R.id.nextDepartureFromRoot)
        val nextDepartureFromDest: TextView = mView.findViewById(R.id.nextDepartureFromDest)

        override fun toString(): String {
            return super.toString() + " '" + cardNameOfBusLineTextVIew.text + "'"
        }
    }
}
