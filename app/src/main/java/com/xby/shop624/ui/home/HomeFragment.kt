package com.xby.shop624.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.xby.shop624.databinding.FragmentHomeBinding
import com.xby.shop624.ui.cart.CartViewModel
import com.xby.shop624.ui.detail.ProductDetailActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private val cartViewModel: CartViewModel by activityViewModels()
    private val bannerAdapter = BannerAdapter()
    private val navGridAdapter = NavGridAdapter { item ->
        viewModel.selectCategory(item.categoryKey)
    }
    private val productAdapter = HomeProductAdapter(
        onItemClick = { product ->
            startActivity(
                Intent(requireContext(), ProductDetailActivity::class.java)
                    .putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, product.id)
            )
        },
        onAddCartClick = { product ->
            cartViewModel.addToCart(product.id)
        }
    )

    private val bannerHandler = Handler(Looper.getMainLooper())
    private var bannerRunnable: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBanner()
        setupNavGrid()
        setupSearch()
        setupProducts()

        viewModel.banners.observe(viewLifecycleOwner) { banners ->
            bannerAdapter.submitList(banners)
            setupBannerIndicators(banners.size)
            startBannerAutoScroll()
        }

        viewModel.navCategories.observe(viewLifecycleOwner) { categories ->
            navGridAdapter.submitList(categories)
        }

        viewModel.selectedCategoryKey.observe(viewLifecycleOwner) { key ->
            navGridAdapter.setSelectedCategoryKey(key)
        }

        viewModel.products.observe(viewLifecycleOwner) { products ->
            productAdapter.submitList(products)
        }

        viewModel.emptyHint.observe(viewLifecycleOwner) { hint ->
            binding.tvEmptyHint.isVisible = hint != null
            binding.tvEmptyHint.text = hint
            binding.rvProducts.isVisible = hint == null
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBar.isVisible = loading
        }

        cartViewModel.toastMessage.observe(viewLifecycleOwner) { msg ->
            msg ?: return@observe
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            cartViewModel.clearToast()
        }
    }

    private fun setupBanner() {
        binding.vpBanner.adapter = bannerAdapter
        binding.vpBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateBannerIndicator(position)
            }
        })
    }

    private fun setupNavGrid() {
        binding.rvNavGrid.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.rvNavGrid.adapter = navGridAdapter
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            override fun afterTextChanged(s: Editable?) {
                viewModel.onSearchQueryChanged(s?.toString().orEmpty())
            }
        })
    }

    private fun setupProducts() {
        binding.rvProducts.adapter = productAdapter
        binding.rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    private fun setupBannerIndicators(count: Int) {
        binding.indicatorContainer.removeAllViews()
        if (count <= 1) return
        repeat(count) { index ->
            val dot = View(requireContext()).apply {
                layoutParams = ViewGroup.MarginLayoutParams(16, 16).apply {
                    marginEnd = if (index < count - 1) 8 else 0
                }
                setBackgroundResource(
                    if (index == 0) com.xby.shop624.R.drawable.bg_indicator_selected
                    else com.xby.shop624.R.drawable.bg_indicator_normal
                )
            }
            binding.indicatorContainer.addView(dot)
        }
    }

    private fun updateBannerIndicator(position: Int) {
        for (i in 0 until binding.indicatorContainer.childCount) {
            binding.indicatorContainer.getChildAt(i).setBackgroundResource(
                if (i == position) com.xby.shop624.R.drawable.bg_indicator_selected
                else com.xby.shop624.R.drawable.bg_indicator_normal
            )
        }
    }

    private fun startBannerAutoScroll() {
        stopBannerAutoScroll()
        if (bannerAdapter.itemCount <= 1) return
        bannerRunnable = object : Runnable {
            override fun run() {
                val count = bannerAdapter.itemCount
                if (count > 1) {
                    val next = (binding.vpBanner.currentItem + 1) % count
                    binding.vpBanner.setCurrentItem(next, true)
                }
                bannerHandler.postDelayed(this, 3000)
            }
        }
        bannerHandler.postDelayed(bannerRunnable!!, 3000)
    }

    private fun stopBannerAutoScroll() {
        bannerRunnable?.let { bannerHandler.removeCallbacks(it) }
        bannerRunnable = null
    }

    override fun onPause() {
        super.onPause()
        stopBannerAutoScroll()
    }

    override fun onResume() {
        super.onResume()
        if (bannerAdapter.itemCount > 1) {
            startBannerAutoScroll()
        }
    }

    override fun onDestroyView() {
        stopBannerAutoScroll()
        super.onDestroyView()
        _binding = null
    }
}
