package eu.espeo.ipg

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PaymentActivity: AppCompatActivity() {

    private val webView by lazy { WebView(this) }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(webView)
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(JSInterface(this), "JSInterface")
        webView.loadUrl("file:///android_asset/index.html")
    }

    private fun finishWithResult(result: Int) {
        setResult(result, null)
        finish()
    }

    private inner class JSInterface(private val context: Context) {

        @JavascriptInterface
        fun paymentSuccessful() {
            finishWithResult(PAYMENT_RESULT_SUCCESS)
        }

        @JavascriptInterface
        fun paymentCancelled() {
            finishWithResult(PAYMENT_RESULT_CANCELLED)
        }

        @JavascriptInterface
        fun paymentFailed() {
            finishWithResult(PAYMENT_RESULT_FAILED)
        }

    }

    companion object {
        const val PAYMENT_REQUEST_CODE = 0
        const val PAYMENT_RESULT_SUCCESS = 0
        const val PAYMENT_RESULT_CANCELLED = 1
        const val PAYMENT_RESULT_FAILED = 2
    }

}