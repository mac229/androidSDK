package com.evopayments.demo.app

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.evopayments.demo.R
import com.evopayments.demo.api.model.PaymentDataResponse
import com.evopayments.sdk.EvoPaymentsCallback
import com.evopayments.sdk.PaymentDialogFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), EvoPaymentsCallback {

    private val viewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startPaymentButton.setOnClickListener { fetchToken() }
    }

    private fun fetchToken() {
        viewModel.fetchToken(CUSTOMER_ID, MERCHANT_ID, this::startPaymentProcess, this::onError)
    }

    private fun startPaymentProcess(data: PaymentDataResponse) {
        val dialogFragment = PaymentDialogFragment.newInstance(MERCHANT_ID, data.cashierUrl, data.token)
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .add(dialogFragment, PaymentDialogFragment.TAG)
            .commit()
    }

    private fun onError() {
        showToast(R.string.failed_starting_payment_process)
    }

    override fun onPaymentSuccessful() {
        PaymentSuccessfulDialogFragment.newInstance().show(supportFragmentManager, null)
    }

    override fun onPaymentCancelled() {
        showToast(R.string.payment_cancelled)
    }

    override fun onPaymentFailed() {
        PaymentFailedDialogFragment.newInstance().show(supportFragmentManager, null)
    }

    override fun onSessionExpired() {
        showToast(R.string.session_expired)
    }

    private fun showToast(@StringRes text: Int) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val MERCHANT_ID = 666L
        private const val CUSTOMER_ID = "sample-002"
    }
}
