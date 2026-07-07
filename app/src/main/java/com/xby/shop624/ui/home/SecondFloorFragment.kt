package com.xby.shop624.ui.home

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xby.shop624.databinding.FragmentSecondFloorBinding
import com.xby.shop624.databinding.ItemSecondFloorProductBinding
import com.xby.shop624.data.local.entity.ProductEntity
import com.xby.shop624.util.loadNetworkImage

class SecondFloorFragment : Fragment() {

    private lateinit var binding: FragmentSecondFloorBinding
    private lateinit var countDownTimer: CountDownTimer
    private var recommendAdapter: SecondFloorRecommendAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSecondFloorBinding.inflate(inflater, container, false)
        initViews()
        startCountDown()
        return binding.root
    }

    private fun initViews() {
        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        recommendAdapter = SecondFloorRecommendAdapter()
        binding.rvRecommend.layoutManager = LinearLayoutManager(context)
        binding.rvRecommend.adapter = recommendAdapter

        val recommendProducts = listOf(
            ProductEntity(101, "农夫山泉550ml×24瓶", "19.9", "26.0", "特价", "💧", "drawable://img", "drink", "water", "限时特价", 5000, ""),
            ProductEntity(102, "康师傅冰红茶500ml×15瓶", "28.0", "35.0", "爆款", "🧋", "drawable://img", "drink", "tea", "热销爆款", 8000, ""),
            ProductEntity(103, "乐事薯片75g×12包", "25.0", "32.0", "特惠", "🍟", "drawable://img", "snack", "chip", "超值特惠", 3000, ""),
            ProductEntity(104, "蒙牛纯牛奶250ml×24盒", "45.0", "55.0", "推荐", "🥛", "drawable://img", "milk", "pure", "品质保证", 6000, "")
        )
        recommendAdapter?.submitList(recommendProducts)
    }

    private fun startCountDown() {
        val endTime = System.currentTimeMillis() + 3600000
        countDownTimer = object : CountDownTimer(endTime - System.currentTimeMillis(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val hours = (millisUntilFinished / 3600000).toInt()
                val minutes = ((millisUntilFinished % 3600000) / 60000).toInt()
                val seconds = ((millisUntilFinished % 60000) / 1000).toInt()
                binding.tvHour.text = String.format("%02d", hours)
                binding.tvMinute.text = String.format("%02d", minutes)
                binding.tvSecond.text = String.format("%02d", seconds)
            }

            override fun onFinish() {
                binding.tvHour.text = "00"
                binding.tvMinute.text = "00"
                binding.tvSecond.text = "00"
            }
        }
        countDownTimer.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer.cancel()
    }

    class SecondFloorRecommendAdapter : RecyclerView.Adapter<SecondFloorRecommendAdapter.ViewHolder>() {

        private val items = mutableListOf<ProductEntity>()

        fun submitList(products: List<ProductEntity>) {
            items.clear()
            items.addAll(products)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemSecondFloorProductBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemCount(): Int = items.size

        class ViewHolder(private val binding: ItemSecondFloorProductBinding) :
            RecyclerView.ViewHolder(binding.root) {

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
                binding.tvTag.text = product.tag
            }
        }
    }
}