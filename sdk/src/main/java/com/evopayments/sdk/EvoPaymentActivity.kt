package com.evopayments.sdk

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity

fun Activity.startEvoPaymentActivityForResult() {

}

class EvoPaymentActivity: AppCompatActivity(), EvoPaymentsCallback {



    override fun onPaymentStarted() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPaymentSuccessful() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPaymentCancelled() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPaymentFailed() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPaymentUndetermined() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}