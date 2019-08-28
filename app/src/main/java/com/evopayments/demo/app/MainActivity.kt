package com.evopayments.demo.app

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.evopayments.demo.R
import com.evopayments.demo.api.model.PaymentDataResponse
import com.evopayments.sdk.EvoPaymentsCallback
import com.evopayments.sdk.PaymentDialogFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), EvoPaymentsCallback {

    private val viewModel by lazy { ViewModelProviders.of(this)[MainViewModel::class.java] }
    private var merchantId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startPaymentButton.setOnClickListener { fetchToken() }
        showTestButton.setOnClickListener { showTest() }
        //defaults
        merchantIdEditText.setTextKeepState("167885")
        passwordEditText.setTextKeepState("56789")
        customerIdEditText.setTextKeepState("lovelyrita")
        currencyEditText.setTextKeepState("PLN")
        countryEditText.setTextKeepState("PL")
        amountEditText.setTextKeepState("2")
    }

    private fun fetchToken() {
        this.merchantId = merchantIdEditText.getText().toString()
        val tokenParams = hashMapOf(
            Pair("merchantId", merchantId),
            Pair("password", passwordEditText.getText().toString()),
            Pair("customerId", customerIdEditText.getText().toString()),
            Pair("currency", currencyEditText.getText().toString()),
            Pair("country", countryEditText.getText().toString()),
            Pair("amount", amountEditText.getText().toString()),
            Pair("action", actionSpinner.selectedItem.toString()),
            Pair("allowOriginUrl", "http://example.com")
            )

        viewModel.fetchToken(tokenParams, this::startPaymentProcess, this::onError)
    }

    private fun showTest() {
        val dialogFragment = PaymentDialogFragment.newInstance("", TEST_CASHIER_URL, "")
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .add(dialogFragment, PaymentDialogFragment.TAG)
            .commit()
    }

    private fun startPaymentProcess(data: PaymentDataResponse) {
        val dialogFragment = PaymentDialogFragment.newInstance(merchantId, data.cashierUrl, data.token)
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

    override fun onRedirected() {
        showToast(R.string.redirection_requested)
    }

    override fun onClose() {
        showToast(R.string.close_requested)
    }

    private fun showToast(@StringRes text: Int) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }


    companion object {
        const val TEST_CASHIER_URL = "https://cashierui-responsivedev.test.myriadpayments.com/react-frontend/index.html"
    }
}
