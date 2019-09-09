package com.evopayments.sdk

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.fragment.app.DialogFragment
import com.evopayments.evocashierlib.R
import java.util.concurrent.TimeUnit

@SuppressLint("SetJavaScriptEnabled", "AddJavascriptInterface")
class PaymentDialogFragment : DialogFragment() {

    private lateinit var paymentCallback: EvoPaymentsCallback

    private val webView by lazy {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            addJavascriptInterface(JSInterface(), "JSInterface")
            webViewClient = PaymentWebViewClient()
        }
    }

    private val timeoutInMs by lazy { arguments!!.getLong(EXTRA_TIMEOUT_IN_MS) }
    private val handler by lazy { Handler() }
    private val sessionExpiredRunnable by lazy { Runnable(this::onSessionExpired) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        paymentCallback = getListenerOrThrowException()
    }

    override fun getTheme(): Int {
        return R.style.PaymentDialogStyle
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return webView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val merchantId = arguments!!.getString(EXTRA_MERCHANT_ID)
        val baseUrl = arguments!!.getString(EXTRA_URL)!!
        val token = arguments!!.getString(EXTRA_TOKEN)
        val myriadFlowId =arguments!!.getString(MYRIAD_FLOW_ID)

        val url = createUrl(baseUrl, merchantId, token, myriadFlowId)
        webView.loadUrl(url)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(sessionExpiredRunnable, timeoutInMs)
    }

    private fun createUrl(baseUrl: String, merchantId: String?, token: String?, myriadFlowId: String): String {
        return Uri.parse(baseUrl)
            .buildUpon()
            .appendQueryParameter(MERCHANT_ID, merchantId.toString())
            .appendQueryParameter(TOKEN, token)
            .appendQueryParameter(MYRIAD_FLOW_ID, myriadFlowId)
            .build()
            .toString()
    }

    private fun onSessionExpired() {
        paymentCallback.onSessionExpired()
        dismiss()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(sessionExpiredRunnable)
    }

    private inner class JSInterface {

        @JavascriptInterface
        fun paymentStarted() {
            paymentCallback.onPaymentStarted()
            dismiss()
        }

        @JavascriptInterface
        fun paymentSuccessful() {
            paymentCallback.onPaymentSuccessful()
            dismiss()
        }

        @JavascriptInterface
        fun paymentCancelled() {
            paymentCallback.onPaymentCancelled()
            dismiss()
        }

        @JavascriptInterface
        fun paymentFailed() {
            paymentCallback.onPaymentFailed()
            dismiss()
        }

        @JavascriptInterface
        fun paymentUndetermined() {
            paymentCallback.onPaymentUndetermined()
            dismiss()
        }

        @JavascriptInterface
        fun redirected(url: String) {
            paymentCallback.onRedirected(url)
            dismiss()
        }

        @JavascriptInterface
        fun close() {
            paymentCallback.onClose()
            dismiss()
        }
    }

    private inner class PaymentWebViewClient : WebViewClient() {
        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
            super.onReceivedError(view, request, error)
            paymentCallback.onPaymentFailed()
            dismiss()
        }
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

        private val DEFAULT_TIMEOUT = TimeUnit.MINUTES.toMillis(10)

        fun newInstance(merchantId: String, cashierUrl: String, token: String, myriadFlowId: String, timeoutInMs: Long = DEFAULT_TIMEOUT) =
            PaymentDialogFragment().apply {
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