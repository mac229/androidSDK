package com.evopayments.ipgcashierlib

interface IpgPaymentCallback {

    fun onPaymentSuccessful()

    fun onPaymentCancelled()

    fun onPaymentFailed()

    fun onSessionExpired() = Unit
}