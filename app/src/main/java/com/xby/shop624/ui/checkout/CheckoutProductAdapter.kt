package com.xby.shop624.ui.checkout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xby.shop624.databinding.ItemCheckoutProductBinding
import com.xby.shop624.model.CartItemUi
import com.xby.shop624.util.PriceUtil
import com.xby.shop624.util.loadNetworkImage

class CheckoutProductAdapter : RecyclerView.Adapter<CheckoutProductAdapter.ViewHolder>() {

    private val items = mutableListOf<CartItemUi>()

    fun submitList(list: List<CartItemUi>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCheckoutProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(
        private val binding: ItemCheckoutProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartItemUi) {
            binding.ivProduct.loadNetworkImage(item.imageUrl)
            binding.tvName.text = item.name
            binding.tvPriceQty.text = "进货¥${PriceUtil.format(item.price)} × ${item.quantity}"
            binding.tvSubtotal.text = "¥${PriceUtil.format(item.subtotal)}"
        }
    }
}
