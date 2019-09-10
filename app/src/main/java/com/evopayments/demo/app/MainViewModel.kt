package com.evopayments.demo.app

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evopayments.demo.api.Communication
import com.evopayments.demo.api.model.DemoTokenParameters
import com.evopayments.demo.api.model.PaymentDataResponse
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.Map as Map1

/**
 * Created by Maciej Kozłowski on 2019-08-01.
 */

class MainViewModel : ViewModel() {



    fun fetchToken(tokenUrl:String, tokenParams: DemoTokenParameters, onSuccess: (PaymentDataResponse) -> Unit, onError: () -> Unit) {
        Communication.reinit(tokenUrl)
        val apiService = Communication.apiService

        viewModelScope.launch {
            try {
                val result = apiService.getToken(tokenParams)
                onSuccess(result)
            } catch (exception: Exception) {
                Log.e(TAG, "Error", exception)
                onError()
            }
        }
    }

    fun resolveCashierUrl(customUrl: String): String {
        return if (customUrl.isBlank())  TEST_CASHIER_URL else customUrl;
    }

    companion object {
        const val TEST_CASHIER_URL = "https://cashierui-responsivedev.test.myriadpayments.com/react-frontend/index.html"
        private val TAG = MainViewModel::class.java.simpleName
    }

}