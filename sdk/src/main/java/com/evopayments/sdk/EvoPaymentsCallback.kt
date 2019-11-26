package com.evopayments.sdk

@Deprecated("EvoPaymentsCallback is deprecated in favor of getting result from EvoPaymentActivity")
interface EvoPaymentsCallback {

    fun onPaymentStarted()

    fun onPaymentSuccessful()

    fun onPaymentCancelled()

    fun onPaymentFailed()

    fun onPaymentUndetermined()

    fun onSessionExpired() = Unit
}