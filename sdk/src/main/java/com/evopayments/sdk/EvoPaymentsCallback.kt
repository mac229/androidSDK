package com.evopayments.sdk

import com.google.android.gms.wallet.PaymentDataRequest

@Deprecated("EvoPaymentsCallback is deprecated in favor of getting result from EvoPaymentActivity")
interface EvoPaymentsCallback {

    fun onPaymentStarted()

    fun onPaymentSuccessful()

    fun onPaymentCancelled()

    fun onPaymentFailed()

    fun onPaymentUndetermined()

    fun handleGPayRequest(request: PaymentDataRequest)

    fun onSessionExpired() = Unit
}