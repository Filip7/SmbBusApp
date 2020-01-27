package tvz.filip.milkovic.smbraspored.ui.screens.busLineDetail.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_bus_line_detail.*
import tvz.filip.milkovic.smbraspored.R
import tvz.filip.milkovic.smbraspored.shared.model.Model
import tvz.filip.milkovic.smbraspored.ui.screens.busLineCard.view.BusLineCardFragment
import tvz.filip.milkovic.smbraspored.ui.screens.busLineDetail.contract.BusLineDetailContract
import tvz.filip.milkovic.smbraspored.ui.screens.busLineDetail.presenter.BusLineDetailFragmentPresenter
import tvz.filip.milkovic.smbraspored.ui.screens.busLineDetail.tabLayout.ViewPagerAdapter


class BusLineDetailFragment : Fragment(), BusLineDetailContract.BusLineDetailView {

    private var unbinder: Unbinder? = null

    private val args: BusLineDetailFragmentArgs by navArgs()

    private var busLine: Model.BusLine? = null

    private var presenter: BusLineDetailContract.BusLineDetailPresenter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_bus_line_detail, container, false)
        unbinder = ButterKnife.bind(this, root!!)

        presenter = BusLineDetailFragmentPresenter(this)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        busLine = args.selectedBusLine

        var card = BusLineCardFragment.newInstance(busLine!!)
        fragmentManager!!.beginTransaction().apply {
            replace(R.id.placeholderForCards, card)
            commit()
        }

        activity!!.toolbar.title =
            presenter?.getTitleWithBusLineCode(activity!!.toolbar.title.toString(), busLine!!.code)

        val adapter = ViewPagerAdapter(context!!, childFragmentManager)
        adapter.busLine = busLine

        activity!!.viewPager.adapter = adapter
        activity!!.tabLayout.setupWithViewPager(activity!!.viewPager)

        itemsswipetorefresh.setOnRefreshListener {
            card = BusLineCardFragment.newInstance(busLine!!)
            fragmentManager!!.beginTransaction().apply {
                replace(R.id.placeholderForCards, card)
                commit()
            }

            itemsswipetorefresh.isRefreshing = false
        }

        presenter!!.downloadBusLinePdf(busLine!!.code, context!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder!!.unbind()
    }

    @OnClick(R.id.fab)
    internal fun onOpenPdfFabClicked() {
        presenter?.openPdfDocument(busLine!!.code, context!!, activity!!)
    }

}
