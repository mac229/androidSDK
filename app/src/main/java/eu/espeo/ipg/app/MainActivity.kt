package eu.espeo.ipg.app

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import eu.espeo.ipg.R
import com.evopayments.ipgcashierlib.IpgPaymentCallback
import com.evopayments.ipgcashierlib.PaymentDialogFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), IpgPaymentCallback, CoroutineScope {

    private val job by lazy { Job() }
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private val repository by lazy { MainRepository(coroutineContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startPaymentButton.setOnClickListener { fetchToken() }
    }

    private fun fetchToken() {
        repository.fetchToken(CUSTOMER_ID, MERCHANT_ID, this::startPaymentProcess, this::onError)
    }

    private fun startPaymentProcess(token: String) {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .add(PaymentDialogFragment.newInstance(MERCHANT_ID, token), PaymentDialogFragment.TAG)
            .commit()
    }

    private fun onError() {
        showToast(R.string.failed_starting_payment_process)
    }

    override fun onPaymentSuccessful() {
        PaymentSuccessfulDialogFragment.newInstance().show(supportFragmentManager, null)
    }

    override fun onPaymentCancelled() {
        showToast(R.string.payment_cancelled)
    }

    override fun onPaymentFailed() {
        PaymentFailedDialogFragment.newInstance().show(supportFragmentManager, null)
    }

    override fun onSessionExpired() {
        showToast(R.string.session_expired)
    }

    private fun showToast(@StringRes text: Int) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        job.cancel()
    }

    companion object {
        private const val MERCHANT_ID = 666L
        private const val CUSTOMER_ID = "sample-002"
    }
}
