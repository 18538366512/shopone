package com.xby.shop624.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xby.shop624.databinding.ItemHomeProductBinding
import com.xby.shop624.data.local.entity.ProductEntity
import com.xby.shop624.util.loadNetworkImage

class HomeProductAdapter(
    private val onItemClick: (ProductEntity) -> Unit = {},
    private val onAddCartClick: (ProductEntity) -> Unit = {}
) : RecyclerView.Adapter<HomeProductAdapter.ProductViewHolder>() {

    private val items = mutableListOf<ProductEntity>()

    fun submitList(products: List<ProductEntity>) {
        items.clear()
        items.addAll(products)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemHomeProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ProductViewHolder(binding, onItemClick, onAddCartClick)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ProductViewHolder(
        private val binding: ItemHomeProductBinding,
        private val onItemClick: (ProductEntity) -> Unit,
        private val onAddCartClick: (ProductEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductEntity) {
            binding.ivProduct.loadNetworkImage(product.imageUrl)
            binding.tvName.text = product.name
            binding.tvPrice.text = binding.root.context.getString(
                com.xby.shop624.R.string.price_wholesale,
                product.price
            )
            binding.tvOriginalPrice.text =
                binding.root.context.getString(
                    com.xby.shop624.R.string.price_retail,
                    product.originalPrice
                )
            binding.tvTag.text = product.tag
            binding.root.setOnClickListener { onItemClick(product) }
            binding.btnAddCart.setOnClickListener { onAddCartClick(product) }
        }
    }
}
