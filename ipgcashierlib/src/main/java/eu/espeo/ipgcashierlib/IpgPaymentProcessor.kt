package eu.espeo.ipgcashierlib

class IpgPaymentProcessor(
    private val merchantId: Long
) {

    fun startPaymentProcess(ipgPaymentCallback: IpgPaymentCallback) {
        val transaction = ipgPaymentCallback.supportFragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        PaymentDialogFragment().show(transaction, "PaymentDialogFragment")
    }

}