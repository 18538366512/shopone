package com.xby.shop624.ui.checkout

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.xby.shop624.R
import com.xby.shop624.databinding.ActivityCheckoutBinding
import com.xby.shop624.model.PaymentMethod
import com.xby.shop624.ui.order.OrderListActivity
import com.xby.shop624.ui.payment.PaymentProgressDialog
import com.xby.shop624.util.PriceUtil

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding
    private val viewModel: CheckoutViewModel by viewModels()
    private val adapter = CheckoutProductAdapter()
    private var paymentDialog: PaymentProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.rvItems.layoutManager = LinearLayoutManager(this)
        binding.rvItems.adapter = adapter

        binding.btnPay.setOnClickListener {
            val method = getSelectedPaymentMethod()
            val user = viewModel.user.value
            val total = viewModel.totalAmount.value?.toDoubleOrNull() ?: 0.0
            if (method == PaymentMethod.BALANCE && user != null && user.balance < total) {
                Toast.makeText(this, R.string.payment_balance_insufficient, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            paymentDialog = PaymentProgressDialog(this, method) {
                viewModel.pay(method)
            }.also { it.show() }
        }

        viewModel.items.observe(this) { items ->
            adapter.submitList(items)
            if (items.isEmpty()) {
                finish()
            }
        }

        viewModel.user.observe(this) { user ->
            user ?: return@observe
            binding.tvShopName.text = user.shopName
            binding.tvShopAddress.text = user.shopAddress
            binding.tvBalanceHint.text =
                getString(R.string.checkout_balance_hint, PriceUtil.format(user.balance))
        }

        viewModel.totalAmount.observe(this) { total ->
            binding.tvTotalAmount.text = "¥$total"
            binding.tvBottomTotal.text = getString(R.string.checkout_bottom_total, total)
        }

        viewModel.deliveryHint.observe(this) { hint ->
            binding.tvDeliveryHint.text = hint
        }

        viewModel.toastMessage.observe(this) { msg ->
            msg ?: return@observe
            paymentDialog?.dismiss()
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }

        viewModel.paySuccess.observe(this) { success ->
            if (!success) return@observe
            paymentDialog?.dismiss()
            Toast.makeText(this, R.string.payment_success_toast, Toast.LENGTH_LONG).show()
            viewModel.clearPaySuccess()
            startActivity(Intent(this, OrderListActivity::class.java))
            finish()
        }

        viewModel.loading.observe(this) { loading ->
            binding.btnPay.isEnabled = !loading
        }
    }

    override fun onDestroy() {
        paymentDialog?.dismiss()
        super.onDestroy()
    }

    private fun getSelectedPaymentMethod(): PaymentMethod {
        return when (binding.rgPayment.checkedRadioButtonId) {
            R.id.rb_alipay -> PaymentMethod.ALIPAY
            R.id.rb_wechat -> PaymentMethod.WECHAT
            R.id.rb_cod -> PaymentMethod.COD
            else -> PaymentMethod.BALANCE
        }
    }
}
