package com.xby.shop624.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xby.shop624.databinding.ItemBannerBinding
import com.xby.shop624.data.local.entity.BannerEntity
import com.xby.shop624.util.loadNetworkImage

class BannerAdapter : ListAdapter<BannerEntity, BannerAdapter.BannerViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = ItemBannerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class BannerViewHolder(
        private val binding: ItemBannerBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(banner: BannerEntity) {
            binding.tvBannerTitle.text = banner.title
            binding.tvBannerSubtitle.text = banner.subtitle
            binding.ivBanner.loadNetworkImage(banner.imageUrl)
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<BannerEntity>() {
        override fun areItemsTheSame(oldItem: BannerEntity, newItem: BannerEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: BannerEntity, newItem: BannerEntity) =
            oldItem == newItem
    }
}
