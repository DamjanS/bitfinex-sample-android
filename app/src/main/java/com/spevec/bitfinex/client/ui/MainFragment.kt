package com.spevec.bitfinex.client.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.spevec.bitfinex.client.R
import com.spevec.bitfinex.client.network.ConnectivityMonitor
import com.spevec.bitfinex.client.ui.adapter.OrderBookAdapter
import com.spevec.bitfinex.client.ui.model.MainResponse
import com.spevec.bitfinex.client.ui.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    companion object {

        const val TAG = "MainFragment"

        @LayoutRes
        private val LAYOUT_RESOURCE: Int = R.layout.fragment_main

        fun newInstance(): Fragment = MainFragment()
    }

    private val mainViewModel: MainViewModel by viewModel()
    private val connectivityMonitor: ConnectivityMonitor by inject()

    private lateinit var orderBookAdapter: OrderBookAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(LAYOUT_RESOURCE, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        orderBookAdapter = OrderBookAdapter(layoutInflater)

        orderBookRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = orderBookAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainViewModel.mainResponse().observe(viewLifecycleOwner, Observer { response -> render(response) })
        savedInstanceState ?: mainViewModel.getMainResponse()
    }

    private fun render(mainResponse: MainResponse) {
        with (mainResponse.ticker) {
            tickerPrice.text = context?.getString(R.string.bitfinex_last_price, lastPrice)
            tickerVolume.text = context?.getString(R.string.bitfinex_volume, volume)
            tickerChange.text = context?.getString(R.string.bitfinex_change, dailyChange)
            tickerHigh.text = context?.getString(R.string.bitfinex_high, high)
            tickerLow.text = context?.getString(R.string.bitfinex_low, low)
        }

        orderBookAdapter.submitList(mainResponse.orderBooks)
    }

    override fun onResume() {
        super.onResume()
        connectListener()
    }

    override fun onPause() {
        disconnectListener()
        super.onPause()
    }

    private fun connectListener() {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(connectivityMonitor)
        } else {
            val builder = NetworkRequest.Builder()
            connectivityManager.registerNetworkCallback(builder.build(), connectivityMonitor)
        }
    }

    private fun disconnectListener() {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.unregisterNetworkCallback(connectivityMonitor)
        } else {
            connectivityManager.unregisterNetworkCallback(connectivityMonitor)
        }
    }
}