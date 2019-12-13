package com.evopayments.sdk

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import androidx.fragment.app.DialogFragment
import com.evopayments.evocashierlib.R
import com.evopayments.sdk.redirect.RedirectCallback
import com.evopayments.sdk.redirect.WebDialogFragment
import com.google.android.gms.wallet.PaymentDataRequest
import java.util.concurrent.TimeUnit

@Deprecated("PaymentDialogFragment is deprecated in favor of EvoPaymentActivity. It will be removed in the next version.")
class PaymentDialogFragment : DialogFragment(), RedirectCallback {

    private lateinit var paymentCallback: EvoPaymentsCallback
    private var onDismissCallback: OnDismissListener? = null

    private val webView by lazy {
        WebViewFactory.createWebView(context!!, JSInterface(), this::onWebViewError)
    }

    private var redirectDialogFragment: WebDialogFragment? = null

    private val timeoutInMs by lazy { arguments!!.getLong(EXTRA_TIMEOUT_IN_MS) }
    private val handler by lazy { Handler() }
    private val sessionExpiredRunnable by lazy { Runnable(this::onSessionExpired) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        paymentCallback = getListenerOrThrowException()
        onDismissCallback = getListener()
    }

    override fun getTheme(): Int {
        return R.style.PaymentDialogStyle
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return webView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val merchantId = arguments!!.getString(EXTRA_MERCHANT_ID)!!
        val baseUrl = arguments!!.getString(EXTRA_URL)!!
        val token = arguments!!.getString(EXTRA_TOKEN)!!
        val myriadFlowId = arguments!!.getString(MYRIAD_FLOW_ID)!!

        val url = createUrl(baseUrl, merchantId, token, myriadFlowId)
        webView.loadUrl(url)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(sessionExpiredRunnable, timeoutInMs)
    }

    private fun createUrl(
        baseUrl: String,
        merchantId: String,
        token: String,
        myriadFlowId: String
    ): String {
        return Uri.parse(baseUrl)
            .buildUpon()
            .appendQueryParameter(MERCHANT_ID, merchantId)
            .appendQueryParameter(TOKEN, token)
            .appendQueryParameter(MYRIAD_FLOW_ID, myriadFlowId)
            .build()
            .toString()
    }

    private fun onSessionExpired() {
        paymentCallback.onSessionExpired()
        dismiss()
    }

    override fun onWebViewError() {
        paymentCallback.onPaymentUndetermined()
        dismiss()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(sessionExpiredRunnable)
    }

    fun onGooglePaymentSuccess(data: Intent?) {
        val processor = PaymentDataIntentProcessor(data)
        val paymentToken = processor.getToken()

        if (paymentToken != null) {
            sendTokenToWebView(paymentToken)
        } else {
            paymentCallback.onPaymentFailed()
        }
    }

    private fun sendTokenToWebView(token: String) {
        webView.evaluateJavascript("window.JSInterface.onGPayTokenReceived($token);") {
            /* there's no result */
            Log.d("debug", it)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissCallback?.onDismiss()
    }

    private inner class JSInterface {
        private val handler = Handler(Looper.getMainLooper())

        @JavascriptInterface
        fun processGPayPayment(paymentDataRequest: String) {
            val request = PaymentDataRequest.fromJson(paymentDataRequest)
            if (request != null) {
                paymentCallback.handleGPayRequest(request)
            }
        }

        @JavascriptInterface
        fun paymentStarted() {
            handler.post {
                paymentCallback.onPaymentStarted()
                dismiss()
            }
        }

        @JavascriptInterface
        fun paymentSuccessful() {
            handler.post {
                paymentCallback.onPaymentSuccessful()
                dismiss()
            }
        }

        @JavascriptInterface
        fun paymentCancelled() {
            handler.post {
                paymentCallback.onPaymentCancelled()
                dismiss()
            }
        }

        @JavascriptInterface
        fun paymentFailed() {
            handler.post {
                paymentCallback.onPaymentFailed()
                dismiss()
            }
        }

        @JavascriptInterface
        fun paymentUndetermined() {
            handler.post {
                paymentCallback.onPaymentUndetermined()
                dismiss()
            }
        }

        @JavascriptInterface
        fun redirected(url: String) {
            handler.post {
                redirectDialogFragment = WebDialogFragment.newInstance(url).also {
                    childFragmentManager
                        .beginTransaction()
                        .addToBackStack(null)
                        .add(it, WebDialogFragment.TAG)
                        .commit()
                }
            }
        }

        @JavascriptInterface
        fun close() {
            handler.post {
                redirectDialogFragment?.dismiss()
            }
        }
    }

    interface OnDismissListener {
        fun onDismiss()
    }

    companion object {

        val TAG: String = PaymentDialogFragment::class.java.simpleName

        private const val MERCHANT_ID = "merchantId"
        private const val TOKEN = "token"
        private const val MYRIAD_FLOW_ID = "myriadFlowId"

        private const val EXTRA_MERCHANT_ID = "extra_merchant_id"
        private const val EXTRA_URL = "extra_cashier_url"
        private const val EXTRA_TOKEN = "extra_token"
        private const val EXTRA_TIMEOUT_IN_MS = "extra_timeout_in_ms"

        internal val DEFAULT_TIMEOUT = TimeUnit.MINUTES.toMillis(10)

        @Deprecated("PaymentDialogFragment is deprecated in favor of EvoPaymentActivity")
        fun newInstance(
            merchantId: String,
            cashierUrl: String,
            token: String,
            myriadFlowId: String,
            timeoutInMs: Long = DEFAULT_TIMEOUT
        ) = PaymentDialogFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_MERCHANT_ID, merchantId)
                putString(EXTRA_URL, cashierUrl)
                putString(EXTRA_TOKEN, token)
                putString(MYRIAD_FLOW_ID, myriadFlowId)
                putLong(EXTRA_TIMEOUT_IN_MS, timeoutInMs)
            }
        }
    }
}