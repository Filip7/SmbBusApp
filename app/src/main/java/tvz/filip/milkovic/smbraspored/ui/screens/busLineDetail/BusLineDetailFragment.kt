package tvz.filip.milkovic.smbraspored.ui.screens.busLineDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import tvz.filip.milkovic.smbraspored.R

class BusLineDetailFragment : Fragment() {

    companion object {
        fun newInstance() =
            BusLineDetailFragment()
    }

    private lateinit var viewModel: BusLineDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.bus_line_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(BusLineDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
