package eu.espeo.ipgcashierlib

import androidx.fragment.app.FragmentManager

class IpgPaymentProcessor(private val merchantId: Long) {

    fun startPaymentProcess(fragmentManager: FragmentManager) {
        fragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .add(PaymentDialogFragment(), TAG)
            .commit()
    }

    companion object {
        private const val TAG = "PaymentDialogFragment"
    }
}