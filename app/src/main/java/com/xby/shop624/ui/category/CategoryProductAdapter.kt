package com.xby.shop624.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xby.shop624.databinding.ItemCategoryProductBinding
import com.xby.shop624.data.local.entity.ProductEntity
import com.xby.shop624.util.loadNetworkImage

class CategoryProductAdapter(
    private val onItemClick: (ProductEntity) -> Unit
) : RecyclerView.Adapter<CategoryProductAdapter.ViewHolder>() {

    private val items = mutableListOf<ProductEntity>()

    fun submitList(products: List<ProductEntity>) {
        items.clear()
        items.addAll(products)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(
        private val binding: ItemCategoryProductBinding,
        private val onItemClick: (ProductEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductEntity) {
            binding.ivProduct.loadNetworkImage(product.imageUrl)
            binding.tvName.text = product.name
            binding.tvPrice.text = binding.root.context.getString(
                com.xby.shop624.R.string.price_wholesale,
                product.price
            )
            binding.tvOriginalPrice.text = binding.root.context.getString(
                com.xby.shop624.R.string.price_retail,
                product.originalPrice
            )
            binding.root.setOnClickListener { onItemClick(product) }
        }
    }
}
