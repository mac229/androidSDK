package com.evopayments.demo.app

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evopayments.demo.api.Communication
import com.evopayments.demo.api.model.PaymentDataResponse
import kotlinx.coroutines.launch

/**
 * Created by Maciej Kozłowski on 2019-08-01.
 */

class MainViewModel : ViewModel() {

    private val apiService = Communication.apiService

    fun fetchToken(customerId: String, merchantId: Long, onSuccess: (PaymentDataResponse) -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            try {
                val result = apiService.getToken(customerId, merchantId)
                onSuccess(result)
            } catch (exception: Exception) {
                Log.e(TAG, "Error", exception)
                onError()
            }
        }
    }

    companion object {

        private val TAG = MainViewModel::class.java.simpleName
    }
}