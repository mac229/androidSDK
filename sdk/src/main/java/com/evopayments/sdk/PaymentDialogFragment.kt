package com.evopayments.sdk

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
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

        val merchantId = arguments!!.getLong(EXTRA_MERCHANT_ID)
        val baseUrl = arguments!!.getString(EXTRA_URL)!!
        val token = arguments!!.getString(EXTRA_TOKEN)

        val url = createUrl(baseUrl, merchantId, token)
        webView.loadUrl(url)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(sessionExpiredRunnable, timeoutInMs)
    }

    private fun createUrl(baseUrl: String, merchantId: Long, token: String?): String {
        return Uri.parse(baseUrl)
            .buildUpon()
            .appendQueryParameter(MERCHANT_ID, merchantId.toString())
            .appendQueryParameter(TOKEN, token)
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
    }

    companion object {

        val TAG: String = PaymentDialogFragment::class.java.simpleName

        private const val MERCHANT_ID = "merchantId"
        private const val TOKEN = "token"

        private const val EXTRA_MERCHANT_ID = "extra_merchant_id"
        private const val EXTRA_URL = "extra_cashier_url"
        private const val EXTRA_TOKEN = "extra_token"
        private const val EXTRA_TIMEOUT_IN_MS = "extra_timeout_in_ms"

        private val DEFAULT_TIMEOUT = TimeUnit.MINUTES.toMillis(10)

        fun newInstance(merchantId: Long, cashierUrl: String, token: String, timeoutInMs: Long = DEFAULT_TIMEOUT) =
            PaymentDialogFragment().apply {
                arguments = Bundle().apply {
                    putLong(EXTRA_MERCHANT_ID, merchantId)
                    putString(EXTRA_URL, cashierUrl)
                    putString(EXTRA_TOKEN, token)
                    putLong(EXTRA_TIMEOUT_IN_MS, timeoutInMs)
                }
            }
    }
}