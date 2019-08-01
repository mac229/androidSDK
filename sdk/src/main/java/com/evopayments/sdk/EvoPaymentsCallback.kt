package com.evopayments.sdk

interface EvoPaymentsCallback {

    fun onPaymentSuccessful()

    fun onPaymentCancelled()

    fun onPaymentFailed()

    fun onSessionExpired() = Unit
}