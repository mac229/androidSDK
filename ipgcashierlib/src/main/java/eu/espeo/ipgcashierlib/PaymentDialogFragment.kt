package eu.espeo.ipgcashierlib

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.fragment.app.DialogFragment

@SuppressLint("SetJavaScriptEnabled", "AddJavascriptInterface")
class PaymentDialogFragment: DialogFragment() {

    private lateinit var paymentCallback: IpgPaymentCallback

    private val webView by lazy {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            addJavascriptInterface(JSInterface(), "JSInterface")
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        paymentCallback = (context as? IpgPaymentCallback)
            ?: throw NotImplementedError("Calling activity must implement IpgPaymentCallback")
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
        val token = arguments!!.getString(EXTRA_TOKEN)

        val url = Uri.parse(URL)
            .buildUpon()
            .appendQueryParameter(MERCHANT_ID, merchantId.toString())
            .appendQueryParameter(TOKEN, token)
            .build()
            .toString()

        webView.loadUrl(url)
    }

    private inner class JSInterface {

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

    companion object {
        private const val URL = "https://cashierui-responsivedev.test.myriadpayments.com/react-frontend/index.html?stylesSheetUrl=orlen.css"

        private const val MERCHANT_ID = "merchantId"
        private const val TOKEN = "token"

        private const val EXTRA_MERCHANT_ID = "extra_merchant_id"
        private const val EXTRA_TOKEN = "extra_token"

        fun newInstance(merchantId: Long, token: String) = PaymentDialogFragment().apply {
            arguments = Bundle().apply {
                putLong(EXTRA_MERCHANT_ID, merchantId)
                putString(EXTRA_TOKEN, token)
            }
        }
    }
}