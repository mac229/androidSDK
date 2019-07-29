package com.evopayments.sdk

interface IpgPaymentCallback {

    fun onPaymentSuccessful()

    fun onPaymentCancelled()

    fun onPaymentFailed()

    fun onSessionExpired() = Unit
}