package eu.espeo.ipgcashierlib

import androidx.fragment.app.FragmentManager

class IpgPaymentProcessor {

    fun startPaymentProcess(fragmentManager: FragmentManager, merchantId: Long, token: String) {
        fragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .add(PaymentDialogFragment.newInstance(merchantId, token), TAG)
            .commit()
    }

    companion object {
        private const val TAG = "PaymentDialogFragment"
    }
}