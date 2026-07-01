package com.xby.shop624.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xby.shop624.R
import com.xby.shop624.databinding.ItemNavGridBinding
import com.xby.shop624.data.local.entity.NavCategoryEntity
import com.xby.shop624.util.loadNetworkImage
import coil.size.Scale

class NavGridAdapter(
    private val onItemClick: (NavCategoryEntity) -> Unit
) : ListAdapter<NavCategoryEntity, NavGridAdapter.NavViewHolder>(DiffCallback) {

    private var selectedCategoryKey: String = "all"

    fun setSelectedCategoryKey(key: String) {
        if (selectedCategoryKey == key) return
        selectedCategoryKey = key
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavViewHolder {
        val binding = ItemNavGridBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NavViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: NavViewHolder, position: Int) {
        holder.bind(getItem(position), selectedCategoryKey)
    }

    class NavViewHolder(
        private val binding: ItemNavGridBinding,
        private val onItemClick: (NavCategoryEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NavCategoryEntity, selectedKey: String) {
            binding.tvName.text = item.name
            binding.ivIcon.loadNetworkImage(item.imageUrl, scale = Scale.FIT)
            val selected = item.categoryKey == selectedKey
            binding.iconContainer.setBackgroundResource(
                if (selected) R.drawable.bg_nav_icon_selected else R.drawable.bg_nav_icon_normal
            )
            binding.tvName.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    if (selected) R.color.primary else R.color.text_primary
                )
            )
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<NavCategoryEntity>() {
        override fun areItemsTheSame(oldItem: NavCategoryEntity, newItem: NavCategoryEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: NavCategoryEntity, newItem: NavCategoryEntity) =
            oldItem == newItem
    }
}
