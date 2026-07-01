package com.xby.shop624.ui.payment

import android.app.Dialog
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.xby.shop624.R
import com.xby.shop624.databinding.DialogPaymentProgressBinding
import com.xby.shop624.model.PaymentMethod

class PaymentProgressDialog(
    private val activity: FragmentActivity,
    private val paymentMethod: PaymentMethod,
    private val onComplete: () -> Unit
) {

    private var dialog: Dialog? = null
    private val handler = Handler(Looper.getMainLooper())
    private var progress = 0

    fun show() {
        val binding = DialogPaymentProgressBinding.inflate(LayoutInflater.from(activity))
        binding.tvPaymentTitle.text = activity.getString(R.string.payment_processing_with, paymentMethod.label)

        dialog = AlertDialog.Builder(activity)
            .setView(binding.root)
            .setCancelable(false)
            .create()
        dialog?.show()

        val steps = listOf(
            activity.getString(R.string.payment_step_init),
            activity.getString(R.string.payment_step_verify),
            activity.getString(R.string.payment_step_submit),
            activity.getString(R.string.payment_step_done)
        )

        val runnable = object : Runnable {
            override fun run() {
                if (progress >= 100) {
                    dialog?.dismiss()
                    onComplete()
                    return
                }
                progress += 25
                binding.progressPayment.progress = progress
                val stepIndex = (progress / 25) - 1
                if (stepIndex in steps.indices) {
                    binding.tvPaymentStatus.text = steps[stepIndex]
                }
                handler.postDelayed(this, 500)
            }
        }
        handler.postDelayed(runnable, 300)
    }

    fun dismiss() {
        handler.removeCallbacksAndMessages(null)
        dialog?.dismiss()
    }
}
