package eu.espeo.ipg

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import eu.espeo.ipgcashierlib.IpgPaymentCallback
import eu.espeo.ipgcashierlib.IpgPaymentProcessor
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : IpgPaymentCallback() {

    private val paymentProcessor by lazy { IpgPaymentProcessor(666) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startPaymentButton.setOnClickListener {
            paymentProcessor.startPaymentProcess(this)
        }
    }

    override fun paymentSuccessful() {
        PaymentSuccessfulDialog().show(supportFragmentManager, "")
    }

    override fun paymentCancelled() {
        Toast.makeText(this, "Payment cancelled", Toast.LENGTH_SHORT).show()
    }

    override fun paymentFailed() {
        PaymentFailedDialog().show(supportFragmentManager, "")
    }

}
