package com.xby.shop624.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.xby.shop624.R
import com.xby.shop624.databinding.FragmentProfileBinding
import com.xby.shop624.ui.order.OrderListActivity
import com.xby.shop624.util.PriceUtil

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 编辑姓名
        binding.layoutNickname.setOnClickListener {
            showEditDialog(
                title = getString(R.string.profile_edit_name_title),
                hint = getString(R.string.profile_edit_name_hint),
                currentValue = binding.tvNickname.text.toString()
            ) { newValue -> viewModel.updateNickname(newValue) }
        }

        // 编辑店铺名
        binding.layoutShopName.setOnClickListener {
            showEditDialog(
                title = getString(R.string.profile_edit_shop_title),
                hint = getString(R.string.profile_edit_shop_hint),
                currentValue = binding.tvShopName.text.toString()
            ) { newValue -> viewModel.updateShopName(newValue) }
        }

        // 编辑地址
        binding.layoutAddress.setOnClickListener {
            showEditDialog(
                title = getString(R.string.profile_edit_address_title),
                hint = getString(R.string.profile_edit_address_hint),
                currentValue = binding.tvAddress.text.toString()
            ) { newValue -> viewModel.updateShopAddress(newValue) }
        }

        binding.menuOrders.setOnClickListener {
            startActivity(Intent(requireContext(), OrderListActivity::class.java))
        }

        binding.menuStoreSaas.setOnClickListener {
            showInfoDialog(
                getString(R.string.profile_menu_saas),
                getString(R.string.profile_saas_desc)
            )
        }

        binding.menuFranchise.setOnClickListener {
            showInfoDialog(
                getString(R.string.profile_menu_franchise),
                getString(R.string.profile_franchise_desc)
            )
        }

        viewModel.user.observe(viewLifecycleOwner) { user ->
            user ?: return@observe
            binding.tvNickname.text = user.nickname
            binding.tvShopName.text = user.shopName
            binding.tvAddress.text = user.shopAddress
            binding.tvPhone.text = getString(R.string.profile_phone_format, user.phone)
            binding.tvBalance.text = "¥${PriceUtil.format(user.balance)}"
        }

        viewModel.orderCount.observe(viewLifecycleOwner) { count ->
            binding.tvOrderCount.text = count.toString()
        }

        viewModel.toastMessage.observe(viewLifecycleOwner) { msg ->
            msg ?: return@observe
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadProfile()
    }

    private fun showEditDialog(
        title: String,
        hint: String,
        currentValue: String,
        onSubmit: (String) -> Unit
    ) {
        val input = EditText(requireContext()).apply {
            setHint(hint)
            setText(currentValue)
            setSelection(text.length)
            setPadding(32, 32, 32, 32)
            setBackgroundColor(0x0A000000.toInt())
            setTextColor(0xDE000000.toInt())
            setHintTextColor(0x61000000.toInt())
            textSize = 15f
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setView(input)
            .setPositiveButton("确定") { _, _ ->
                val newValue = input.text?.toString().orEmpty()
                onSubmit(newValue)
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun showInfoDialog(title: String, message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.detail_submit) { _, _ ->
                Toast.makeText(requireContext(), R.string.profile_consult_toast, Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(R.string.detail_cancel, null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
