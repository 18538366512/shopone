package com.xby.shop624.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xby.shop624.R
import com.xby.shop624.databinding.ItemSubCategoryBinding
import com.xby.shop624.data.local.entity.SubCategoryEntity

class SubCategoryAdapter(
    private val onItemClick: (SubCategoryEntity) -> Unit
) : ListAdapter<SubCategoryEntity, SubCategoryAdapter.ViewHolder>(DiffCallback) {

    private var selectedKey: String = "all"

    fun setSelectedKey(key: String) {
        if (selectedKey == key) return
        selectedKey = key
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSubCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), selectedKey)
    }

    class ViewHolder(
        private val binding: ItemSubCategoryBinding,
        private val onItemClick: (SubCategoryEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SubCategoryEntity, selectedKey: String) {
            binding.tvName.text = item.name
            val selected = item.subKey == selectedKey
            binding.root.setBackgroundResource(
                if (selected) R.drawable.bg_sub_category_selected else R.drawable.bg_sub_category_normal
            )
            binding.tvName.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    if (selected) R.color.white else R.color.text_secondary
                )
            )
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<SubCategoryEntity>() {
        override fun areItemsTheSame(a: SubCategoryEntity, b: SubCategoryEntity) = a.id == b.id
        override fun areContentsTheSame(a: SubCategoryEntity, b: SubCategoryEntity) = a == b
    }
}
