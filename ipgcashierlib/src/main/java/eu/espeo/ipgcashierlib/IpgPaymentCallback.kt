package eu.espeo.ipgcashierlib

import android.webkit.JavascriptInterface
import androidx.appcompat.app.AppCompatActivity

abstract class IpgPaymentCallback: AppCompatActivity() {
    @JavascriptInterface
    abstract fun paymentSuccessful()

    @JavascriptInterface
    abstract fun paymentCancelled()

    @JavascriptInterface
    abstract fun paymentFailed()
}