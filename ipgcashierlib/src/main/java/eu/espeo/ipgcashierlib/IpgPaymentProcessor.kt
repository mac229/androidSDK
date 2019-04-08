package eu.espeo.ipgcashierlib

class IpgPaymentProcessor(
    private val merchantId: Long
) {

    fun startPaymentProcess(ipgPaymentCallback: IpgPaymentCallback) {
        PaymentFragment().showNow(ipgPaymentCallback.supportFragmentManager, "PaymentFragment")
    }

}