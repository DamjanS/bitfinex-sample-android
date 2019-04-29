package com.spevec.bitfinex.client.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.spevec.bitfinex.client.R
import com.spevec.bitfinex.client.data.model.OrderBook
import kotlinx.android.synthetic.main.recycler_view_item_order_book.view.*

class OrderBookAdapter(private val layoutInflater: LayoutInflater) : ListAdapter<OrderBook, OrderBookAdapter.OrderBookViewHolder>(DiffUtilCallback()) {

    companion object {
        private const val LAYOUT_RESOURCE_ORDER_BOOK_ITEM = R.layout.recycler_view_item_order_book
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderBookViewHolder {
        return OrderBookViewHolder(layoutInflater.inflate(LAYOUT_RESOURCE_ORDER_BOOK_ITEM, parent, false))
    }

    override fun onBindViewHolder(holder: OrderBookViewHolder, position: Int) = holder.fillView(getItem(position))

    inner class OrderBookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun fillView(orderBook: OrderBook) {
            with(itemView) {
                orderBookCount.text = orderBook.count.toString()
                orderBookAsk.text = (orderBook.price + orderBook.count).toString()
                orderBookBid.text = (orderBook.price - orderBook.count).toString()
            }
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<OrderBook>() {

        // we don't really have an ID to compare them - I always submit completely new list of items so it's not worth comparing them
        override fun areItemsTheSame(oldItem: OrderBook, newItem: OrderBook): Boolean = false

        override fun areContentsTheSame(oldItem: OrderBook, newItem: OrderBook): Boolean = false
    }
}