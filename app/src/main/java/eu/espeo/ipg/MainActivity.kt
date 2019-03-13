package eu.espeo.ipg

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startPaymentButton.setOnClickListener {
            startActivityForResult(Intent(this, PaymentActivity::class.java), PaymentActivity.PAYMENT_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            PaymentActivity.PAYMENT_REQUEST_CODE -> {
                when(resultCode) {
                    PaymentActivity.PAYMENT_RESULT_SUCCESS -> PaymentSuccessfulDialog().show(supportFragmentManager, "")
                    PaymentActivity.PAYMENT_RESULT_CANCELLED -> Toast.makeText(this, "Payment cancelled", Toast.LENGTH_SHORT).show()
                    PaymentActivity.PAYMENT_RESULT_FAILED -> PaymentFailedDialog().show(supportFragmentManager, "")
                }
            }
        }
    }
}
