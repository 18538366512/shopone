package com.xby.shop624.ui.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xby.shop624.databinding.ItemOrderBinding
import com.xby.shop624.model.OrderStatus
import com.xby.shop624.model.OrderWithItems
import com.xby.shop624.model.PaymentMethod
import com.xby.shop624.util.PriceUtil
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderAdapter : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    private val items = mutableListOf<OrderWithItems>()

    fun submitList(list: List<OrderWithItems>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(
        private val binding: ItemOrderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(orderWithItems: OrderWithItems) {
            val order = orderWithItems.order
            binding.tvOrderNo.text = "订单号：${order.orderNo}"
            binding.tvStatus.text = OrderStatus.fromCode(order.status).label
            binding.tvDeliveryHint.text = order.deliveryHint
            binding.tvPaymentMethod.text =
                PaymentMethod.fromCode(order.paymentMethod).label
            binding.tvTotal.text = "¥${PriceUtil.format(order.totalAmount)}"

            val summary = orderWithItems.items.joinToString("、") { item ->
                "${item.productName}×${item.quantity}"
            }
            binding.tvItemsSummary.text = summary

            val time = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
                .format(Date(order.createdAt))
            binding.tvPaymentMethod.text =
                "${PaymentMethod.fromCode(order.paymentMethod).label} · $time"
        }
    }
}
