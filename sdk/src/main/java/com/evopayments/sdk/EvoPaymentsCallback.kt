package com.evopayments.sdk

interface EvoPaymentsCallback {

    fun onPaymentStarted()

    fun onPaymentSuccessful()

    fun onPaymentCancelled()

    fun onPaymentFailed()

    fun onPaymentUndetermined()

    fun onRedirected(url: String)

    fun onClose()

    fun onSessionExpired() = Unit


}