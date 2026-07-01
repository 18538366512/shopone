package com.xby.shop624.ui.category

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.xby.shop624.databinding.FragmentCategoryBinding
import com.xby.shop624.ui.detail.ProductDetailActivity

class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CategoryViewModel by viewModels()
    private val primaryAdapter = PrimaryCategoryAdapter { item ->
        viewModel.selectPrimary(item.categoryKey)
    }
    private val subAdapter = SubCategoryAdapter { item ->
        viewModel.selectSub(item.subKey)
    }
    private val productAdapter = CategoryProductAdapter { product ->
        startActivity(
            Intent(requireContext(), ProductDetailActivity::class.java)
                .putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, product.id)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvPrimary.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPrimary.adapter = primaryAdapter

        binding.rvSub.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvSub.adapter = subAdapter

        binding.rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvProducts.adapter = productAdapter

        viewModel.primaryCategories.observe(viewLifecycleOwner) { list ->
            primaryAdapter.submitList(list)
        }

        viewModel.selectedPrimaryKey.observe(viewLifecycleOwner) { key ->
            primaryAdapter.setSelectedKey(key)
        }

        viewModel.subCategories.observe(viewLifecycleOwner) { list ->
            subAdapter.submitList(list)
        }

        viewModel.selectedSubKey.observe(viewLifecycleOwner) { key ->
            subAdapter.setSelectedKey(key)
        }

        viewModel.products.observe(viewLifecycleOwner) { list ->
            productAdapter.submitList(list)
        }

        viewModel.emptyHint.observe(viewLifecycleOwner) { hint ->
            binding.tvEmpty.isVisible = hint != null
            binding.tvEmpty.text = hint
            binding.rvProducts.isVisible = hint == null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
