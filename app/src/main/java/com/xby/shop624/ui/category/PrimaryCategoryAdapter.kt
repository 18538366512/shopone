package com.xby.shop624.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xby.shop624.R
import com.xby.shop624.databinding.ItemPrimaryCategoryBinding
import com.xby.shop624.data.local.entity.PrimaryCategoryEntity
import com.xby.shop624.util.loadNetworkImage
import coil.size.Scale

class PrimaryCategoryAdapter(
    private val onItemClick: (PrimaryCategoryEntity) -> Unit
) : ListAdapter<PrimaryCategoryEntity, PrimaryCategoryAdapter.ViewHolder>(DiffCallback) {

    private var selectedKey: String = ""

    fun setSelectedKey(key: String) {
        if (selectedKey == key) return
        selectedKey = key
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPrimaryCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), selectedKey)
    }

    class ViewHolder(
        private val binding: ItemPrimaryCategoryBinding,
        private val onItemClick: (PrimaryCategoryEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PrimaryCategoryEntity, selectedKey: String) {
            binding.tvName.text = item.name
            binding.ivIcon.loadNetworkImage(item.imageUrl, scale = Scale.FIT)
            val selected = item.categoryKey == selectedKey
            binding.root.setBackgroundResource(
                if (selected) R.drawable.bg_primary_category_selected
                else R.drawable.bg_primary_category_normal
            )
            binding.tvName.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    if (selected) R.color.primary else R.color.text_primary
                )
            )
            binding.indicator.isSelected = selected
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<PrimaryCategoryEntity>() {
        override fun areItemsTheSame(a: PrimaryCategoryEntity, b: PrimaryCategoryEntity) = a.id == b.id
        override fun areContentsTheSame(a: PrimaryCategoryEntity, b: PrimaryCategoryEntity) = a == b
    }
}
