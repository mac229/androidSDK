package com.evopayments.sdk

import android.content.Context
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.view.Gravity
import android.widget.Toast



/**
 * Created by Maciej Kozłowski on 2019-09-10.
 */

@Suppress("SetJavaScriptEnabled", "AddJavascriptInterface")
internal object WebViewFactory {

    fun createWebView(context: Context, jsInterface: Any?, onError: () -> Unit): WebView {
        return WebView(context).apply {
            jsInterface?.let {
                addJavascriptInterface(it, it.javaClass.simpleName)
            }
            settings.javaScriptEnabled = true

            webViewClient = PaymentWebViewClient(onError)
        }
    }

    private class PaymentWebViewClient(private val onError: () -> Unit) : WebViewClient() {
//        override fun onReceivedError(
//            view: WebView?,
//            request: WebResourceRequest?,
//            error: WebResourceError?
//        ) {
//            super.onReceivedError(view, request, error)
//            onError()
//        }
        override fun onReceivedError(
            view: WebView, errorCode: Int,
            description: String, failingUrl: String
        ) {

            val toast = Toast.makeText(
                view.context, description + ' ' + failingUrl,
                Toast.LENGTH_LONG
            )
            toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 0)
            toast.show()
        }
    }

}