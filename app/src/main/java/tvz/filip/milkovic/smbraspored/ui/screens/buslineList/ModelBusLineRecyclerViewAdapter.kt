package tvz.filip.milkovic.smbraspored.ui.screens.buslineList


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_modelbusline.view.*
import tvz.filip.milkovic.smbraspored.R
import tvz.filip.milkovic.smbraspored.shared.model.Model
import tvz.filip.milkovic.smbraspored.ui.screens.buslineList.view.BusLineListFragment.OnListFragmentInteractionListener

/**
 * [RecyclerView.Adapter] that can display a [Model.BusLine] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class ModelBusLineRecyclerViewAdapter(
    private val mValues: List<Model.BusLine>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<ModelBusLineRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Model.BusLine
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.

            mListener?.onListFragmentInteraction(item, v)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_modelbusline, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mIdView.text = item.code
        holder.mContentView.text = item.name

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_number
        val mContentView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
