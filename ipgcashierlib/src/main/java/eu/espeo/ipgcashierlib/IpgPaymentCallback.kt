package eu.espeo.ipgcashierlib

import android.webkit.JavascriptInterface

interface IpgPaymentCallback {

    @JavascriptInterface
    fun paymentSuccessful()

    @JavascriptInterface
    fun paymentCancelled()

    @JavascriptInterface
    fun paymentFailed()
}