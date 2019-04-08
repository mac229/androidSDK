package eu.espeo.ipgcashierlib

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.fragment.app.DialogFragment

@SuppressLint("SetJavaScriptEnabled", "AddJavascriptInterface")
class PaymentFragment: DialogFragment() {

    private lateinit var paymentCallback: IpgPaymentCallback

    private val webView by lazy {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            addJavascriptInterface(JSInterface(context), "JSInterface")

            loadUrl("https://cashierui-responsivedev.test.myriadpayments.com/react-frontend/index.html")
        }
    }

    private inner class JSInterface(context: Context) {

        @JavascriptInterface
        fun paymentSuccessful() {
            paymentCallback.paymentSuccessful()
            dismiss()
        }

        @JavascriptInterface
        fun paymentCancelled() {
            paymentCallback.paymentCancelled()
            dismiss()
        }

        @JavascriptInterface
        fun paymentFailed() {
            paymentCallback.paymentFailed()
            dismiss()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        paymentCallback = (context as? IpgPaymentCallback)
            ?: throw NotImplementedError("Calling activity must implement IpgPaymentCallback")
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return webView
    }

}