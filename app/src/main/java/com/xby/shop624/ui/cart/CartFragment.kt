package com.xby.shop624.ui.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.xby.shop624.databinding.FragmentCartBinding
import com.xby.shop624.ui.checkout.CheckoutActivity
import com.xby.shop624.ui.detail.ProductDetailActivity
class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CartViewModel by activityViewModels()
    private var isUpdatingSelectAll = false

    private val cartAdapter = CartItemAdapter(
        onSelectedChanged = { id, selected -> viewModel.toggleSelected(id, selected) },
        onIncrease = { id -> viewModel.increaseQuantity(id) },
        onDecrease = { id -> viewModel.decreaseQuantity(id) },
        onItemClick = { productId ->
            startActivity(
                Intent(requireContext(), ProductDetailActivity::class.java)
                    .putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, productId)
            )
        }
    )

    private var itemTouchHelper: ItemTouchHelper? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvCart.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCart.adapter = cartAdapter

        itemTouchHelper = ItemTouchHelper(
            CartSwipeCallback(cartAdapter) { id -> viewModel.deleteItem(id) }
        ).also { it.attachToRecyclerView(binding.rvCart) }

        binding.cbSelectAll.setOnCheckedChangeListener { _, checked ->
            if (!isUpdatingSelectAll) {
                viewModel.setSelectAll(checked)
            }
        }

        binding.btnCheckout.setOnClickListener {
            if (viewModel.canCheckout()) {
                startActivity(Intent(requireContext(), CheckoutActivity::class.java))
            }
        }
        viewModel.cartItems.observe(viewLifecycleOwner) { items ->
            cartAdapter.submitList(items)
        }

        viewModel.isEmpty.observe(viewLifecycleOwner) { empty ->
            binding.layoutEmpty.isVisible = empty
            binding.rvCart.isVisible = !empty
            binding.layoutBottom.isVisible = !empty
        }

        viewModel.allSelected.observe(viewLifecycleOwner) { all ->
            isUpdatingSelectAll = true
            binding.cbSelectAll.isChecked = all
            isUpdatingSelectAll = false
        }

        viewModel.selectedTotal.observe(viewLifecycleOwner) { total ->
            binding.tvTotalPrice.text = "¥$total"
        }

        viewModel.selectedCount.observe(viewLifecycleOwner) { count ->
            binding.btnCheckout.text = getString(com.xby.shop624.R.string.cart_checkout_count, count)
        }

        viewModel.toastMessage.observe(viewLifecycleOwner) { msg ->
            msg ?: return@observe
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    override fun onDestroyView() {
        itemTouchHelper?.attachToRecyclerView(null)
        itemTouchHelper = null
        super.onDestroyView()
        _binding = null
    }
}
