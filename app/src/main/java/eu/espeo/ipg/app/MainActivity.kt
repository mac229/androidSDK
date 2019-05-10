package eu.espeo.ipg.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import eu.espeo.ipg.R
import eu.espeo.ipgcashierlib.IpgPaymentCallback
import eu.espeo.ipgcashierlib.IpgPaymentProcessor
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IpgPaymentCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startPaymentButton.setOnClickListener { fetchToken() }
    }

    fun fetchToken() {
        
    }

    private fun startPaymentProcess(token: String) {
        IpgPaymentProcessor().startPaymentProcess(supportFragmentManager, MERCHANT_ID, token)
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

    companion object {
        private const val MERCHANT_ID = 666L
    }
}
