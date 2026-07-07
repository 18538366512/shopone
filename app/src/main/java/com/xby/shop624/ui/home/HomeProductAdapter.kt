package com.xby.shop624.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xby.shop624.databinding.ItemHomeProductBinding
import com.xby.shop624.databinding.ItemLoadingMoreBinding
import com.xby.shop624.data.local.entity.ProductEntity
import com.xby.shop624.util.loadNetworkImage

class HomeProductAdapter(
    private val onItemClick: (ProductEntity) -> Unit = {},
    private val onAddCartClick: (ProductEntity) -> Unit = {}
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<ProductEntity>()
    private var loading = false
    private var hasMore = true

    val isLoading: Boolean get() = loading

    companion object {
        private const val VIEW_TYPE_PRODUCT = 0
        private const val VIEW_TYPE_LOADING = 1
    }

    fun submitList(products: List<ProductEntity>) {
        items.clear()
        items.addAll(products)
        notifyDataSetChanged()
    }

    fun addItems(products: List<ProductEntity>) {
        val startPos = items.size
        items.addAll(products)
        notifyItemRangeInserted(startPos, products.size)
    }

    fun setLoading(loading: Boolean) {
        this.loading = loading
        if (loading) {
            notifyItemInserted(items.size)
        } else {
            notifyItemRemoved(items.size)
        }
    }

    fun setHasMore(more: Boolean) {
        hasMore = more
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == items.size && loading) VIEW_TYPE_LOADING else VIEW_TYPE_PRODUCT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_LOADING -> {
                val binding = ItemLoadingMoreBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                LoadingViewHolder(binding)
            }
            else -> {
                val binding = ItemHomeProductBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                ProductViewHolder(binding, onItemClick, onAddCartClick)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProductViewHolder) {
            holder.bind(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size + if (loading) 1 else 0
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val layoutManager = recyclerView.layoutManager as? GridLayoutManager ?: return
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (getItemViewType(position) == VIEW_TYPE_LOADING) layoutManager.spanCount else 1
            }
        }
    }

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

    class LoadingViewHolder(binding: ItemLoadingMoreBinding) :
        RecyclerView.ViewHolder(binding.root)
}
