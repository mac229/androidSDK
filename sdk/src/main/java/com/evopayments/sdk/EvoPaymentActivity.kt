package com.evopayments.sdk

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

fun Activity.startEvoPaymentActivityForResult(
    requestCode: Int,
    merchantId: String,
    cashierUrl: String,
    token: String,
    myriadFlowId: String,
    timeoutInMs: Long? = null
) {
    startActivityForResult(
        EvoPaymentActivity.createIntent(
            this,
            merchantId,
            cashierUrl,
            token,
            myriadFlowId,
            timeoutInMs
        ),
        requestCode
    )
}

class EvoPaymentActivity : AppCompatActivity(), EvoPaymentsCallback, PaymentDialogFragment.OnDismissListener {

    private val merchantId by lazy { intent.getStringExtra(MERCHANT_ID) }
    private val cashierUrl by lazy { intent.getStringExtra(CASHIER_URL) }
    private val token by lazy { intent.getStringExtra(TOKEN) }
    private val myriadFlowId by lazy { intent.getStringExtra(MYRIAD_FLOW_ID) }
    private val timeoutInMs by lazy {
        intent.getLongExtra(
            TIMEOUT_IN_MS,
            PaymentDialogFragment.DEFAULT_TIMEOUT
        )
    }

    private var isPaymentStarted: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val dialogFragment = PaymentDialogFragment.newInstance(
                merchantId,
                cashierUrl,
                token,
                myriadFlowId,
                timeoutInMs
            )

            supportFragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .add(dialogFragment, PaymentDialogFragment.TAG)
                .commit()
        }
    }

    @Deprecated("This method is public only for a short amount of time. It will be removed in the next release", level = DeprecationLevel.ERROR)
    override fun onPaymentStarted() {
        isPaymentStarted = true
    }

    @Deprecated("This method is public only for a short amount of time. It will be removed in the next release", level = DeprecationLevel.ERROR)
    override fun onPaymentSuccessful() {
        finishWithResult(PAYMENT_SUCCESSFUL)
    }

    @Deprecated("This method is public only for a short amount of time. It will be removed in the next release", level = DeprecationLevel.ERROR)
    override fun onPaymentCancelled() {
        finishWithResult(PAYMENT_CANCELED)
    }

    @Deprecated("This method is public only for a short amount of time. It will be removed in the next release", level = DeprecationLevel.ERROR)
    override fun onPaymentFailed() {
        finishWithResult(PAYMENT_FAILED)
    }

    @Deprecated("This method is public only for a short amount of time. It will be removed in the next release", level = DeprecationLevel.ERROR)
    override fun onPaymentUndetermined() {
        finishWithResult(PAYMENT_UNDETERMINED)
    }

    @Deprecated("This method is public only for a short amount of time. It will be removed in the next release", level = DeprecationLevel.ERROR)
    override fun onSessionExpired() {
        finishWithResult(PAYMENT_SESSION_EXPIRED)
    }

    private fun finishWithResult(resultCode: Int) {
        setResult(resultCode)
        finish()
    }

    override fun onDismiss() {
        finishWithResult(PAYMENT_CANCELED)
    }

    override fun onBackPressed() {
        if(!isPaymentStarted) {
            finishWithResult(PAYMENT_CANCELED)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_PAYMENT_STARTED_EXTRA, isPaymentStarted)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if(savedInstanceState != null) {
            isPaymentStarted = savedInstanceState.getBoolean(IS_PAYMENT_STARTED_EXTRA)
        }
    }

    companion object {
        private const val MERCHANT_ID = "merchant_id"
        private const val CASHIER_URL = "cashier_url"
        private const val TOKEN = "token"
        private const val MYRIAD_FLOW_ID = "myriad_flow_id"
        private const val TIMEOUT_IN_MS = "timeout_in_ms"
        private const val IS_PAYMENT_STARTED_EXTRA = "is_payment_started"

        const val PAYMENT_SUCCESSFUL = 1
        const val PAYMENT_CANCELED = 2
        const val PAYMENT_FAILED = 3
        const val PAYMENT_UNDETERMINED = 4
        const val PAYMENT_SESSION_EXPIRED = 5

        internal fun createIntent(
            context: Context,
            merchantId: String,
            cashierUrl: String,
            token: String,
            myriadFlowId: String,
            timeoutInMs: Long?
        ) = Intent(context, EvoPaymentActivity::class.java).apply {
            putExtra(MERCHANT_ID, merchantId)
            putExtra(CASHIER_URL, cashierUrl)
            putExtra(TOKEN, token)
            putExtra(MYRIAD_FLOW_ID, myriadFlowId)
            putExtra(TIMEOUT_IN_MS, timeoutInMs)
        }


    }


}