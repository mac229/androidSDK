package com.evopayments.demo.app

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.evopayments.demo.R
import com.evopayments.demo.api.Communication
import com.evopayments.demo.api.model.DemoTokenParameters
import com.evopayments.demo.api.model.PaymentDataResponse
import com.evopayments.sdk.EvoPaymentsCallback
import com.evopayments.sdk.PaymentDialogFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), EvoPaymentsCallback {

    private val viewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }
    private var merchantId: String = ""
    private lateinit var myriadFlowId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myriadFlowId = viewModel.generateFlowId()
        startPaymentButton.setOnClickListener { fetchToken() }
        showTestButton.setOnClickListener { showRawWebDemo() }
        setDefaults()
    }

    private fun setDefaults() {
        val defaults = DemoTokenParameters()
        merchantIdEditText.setText(defaults.getMerchantId())
        passwordEditText.setText(defaults.getPassword())
        customerIdEditText.setText(defaults.getCustomerId())
        currencyEditText.setText(defaults.getCurrency())
        countryEditText.setText(defaults.getCountry())
        amountEditText.setText(defaults.getAmount())
        languageEditText.setText(defaults.getLanguage())

        tokenUrlEditText.setText(Communication.getTokenUrl())
    }

    private fun fetchToken() {
        this.merchantId = merchantIdEditText.getValue()
        val tokenParams = DemoTokenParameters(
            merchantId = merchantId,
            password = passwordEditText.getValue(),
            customerId = customerIdEditText.getValue(),
            currency = currencyEditText.getValue(),
            country = countryEditText.getValue(),
            amount = amountEditText.getValue(),
            action = actionSpinner.selectedItem.toString(),
            language = languageEditText.getValue(),
            myriadFlowId = myriadFlowId
        )
        viewModel.fetchToken(tokenUrlEditText.getValue(), tokenParams, this::startPaymentProcess, this::onError)
    }

    private fun EditText.getValue(): String {
        return text.toString()
    }

    private fun showRawWebDemo() {
        val cashierUrl = viewModel.resolveCashierUrl(cashierUrlEditText.getValue())
        val dialogFragment = PaymentDialogFragment.newInstance(
            merchantId = "",
            cashierUrl = cashierUrl,
            token = "",
            myriadFlowId = myriadFlowId
        )
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .add(dialogFragment, PaymentDialogFragment.TAG)
            .commit()
    }

    private fun startPaymentProcess(data: PaymentDataResponse) {
        val dialogFragment = PaymentDialogFragment.newInstance(
            merchantId,
            data.cashierUrl,
            data.token,
            myriadFlowId
        )
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


}
