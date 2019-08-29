package com.evopayments.sdk

interface EvoPaymentsCallback {

    fun onPaymentSuccessful()

    fun onPaymentCancelled()

    fun onPaymentFailed()

    fun onRedirected()

    fun onClose()

    fun onSessionExpired() = Unit


}