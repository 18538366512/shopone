package com.xby.shop624.ui.order

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.xby.shop624.databinding.ActivityOrderListBinding

class OrderListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderListBinding
    private val viewModel: OrderListViewModel by viewModels()
    private val adapter = OrderAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.rvOrders.layoutManager = LinearLayoutManager(this)
        binding.rvOrders.adapter = adapter

        viewModel.orders.observe(this) { orders ->
            adapter.submitList(orders)
            binding.layoutEmpty.isVisible = orders.isEmpty()
            binding.rvOrders.isVisible = orders.isNotEmpty()
        }
    }
}
