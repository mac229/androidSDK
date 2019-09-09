package com.evopayments.demo.app

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.evopayments.demo.R
import com.evopayments.demo.api.Communication
import com.evopayments.demo.api.model.PaymentDataResponse
import com.evopayments.sdk.EvoPaymentsCallback
import com.evopayments.sdk.PaymentDialogFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), EvoPaymentsCallback {

    private val viewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }
    private var merchantId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startPaymentButton.setOnClickListener { fetchToken() }
        showTestButton.setOnClickListener { showRawWebDemo() }
        setDefaults()
    }

    private fun setDefaults() {
        merchantIdEditText.setTextKeepState("167885")
        passwordEditText.setTextKeepState("56789")
        customerIdEditText.setTextKeepState("lovelyrita")
        currencyEditText.setTextKeepState("PLN")
        countryEditText.setTextKeepState("PL")
        amountEditText.setTextKeepState("2")
        tokenUrlEditText.setTextKeepState(Communication.getTokenUrl())
        languageEditText.setTextKeepState("pl")
    }

    private fun fetchToken() {
        this.merchantId = merchantIdEditText.getValue()
        val tokenParams = hashMapOf(
            "merchantId" to merchantId,
            "password" to passwordEditText.getValue(),
            "customerId" to customerIdEditText.getValue(),
            "currency" to currencyEditText.getValue(),
            "country" to countryEditText.getValue(),
            "amount" to amountEditText.getValue(),
            "action" to actionSpinner.selectedItem.toString(),
            "allowOriginUrl" to "http://example.com",
            "language" to languageEditText.getValue()
        )

        Communication.setTokenUrl(tokenUrlEditText.getValue())
        viewModel.fetchToken(tokenParams, this::startPaymentProcess, this::onError)
    }

    private fun EditText.getValue(): String {
        return text.toString()
    }

    private fun showRawWebDemo() {
        var cashierUrl = cashierUrlEditText.getText().toString();
        if(cashierUrl == "") {
            cashierUrl = TEST_CASHIER_URL
        }
        val dialogFragment = PaymentDialogFragment.newInstance("", cashierUrl, "")
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

    override fun onPaymentStarted() {
        showToast(R.string.payment_started)
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

    override fun onPaymentUndetermined() {
        showToast(R.string.payment_result_undetermined)
    }

    override fun onSessionExpired() {
        showToast(R.string.session_expired)
    }

    override fun onRedirected(url: String) {
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
