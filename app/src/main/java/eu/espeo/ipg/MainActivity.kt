package eu.espeo.ipg

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import eu.espeo.ipgcashierlib.IpgPaymentCallback
import eu.espeo.ipgcashierlib.IpgPaymentProcessor
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IpgPaymentCallback {

    private val paymentProcessor by lazy { IpgPaymentProcessor(MERCHANT_ID) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startPaymentButton.setOnClickListener {
            paymentProcessor.startPaymentProcess(supportFragmentManager)
        }
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
