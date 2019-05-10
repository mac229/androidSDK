package eu.espeo.ipg.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import eu.espeo.ipg.R
import eu.espeo.ipgcashierlib.IpgPaymentCallback
import eu.espeo.ipgcashierlib.IpgPaymentProcessor
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
        IpgPaymentProcessor().startPaymentProcess(supportFragmentManager, MERCHANT_ID, token)
    }

    private fun onError() {
        Toast.makeText(this, R.string.failed_starting_payment_process, Toast.LENGTH_SHORT).show()
    }

    override fun paymentSuccessful() {
        PaymentSuccessfulDialog().show(supportFragmentManager, null)
    }

    override fun paymentCancelled() {
        Toast.makeText(this, R.string.payment_cancelled, Toast.LENGTH_SHORT).show()
    }

    override fun paymentFailed() {
        PaymentFailedDialog().show(supportFragmentManager, null)
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
