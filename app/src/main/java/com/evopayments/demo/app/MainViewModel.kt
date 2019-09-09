package com.evopayments.demo.app

import android.util.Log
import android.util.Pair
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evopayments.demo.api.Communication
import com.evopayments.demo.api.model.PaymentDataResponse
import kotlinx.coroutines.launch

/**
 * Created by Maciej Kozłowski on 2019-08-01.
 */

class MainViewModel : ViewModel() {



    fun fetchToken(tokenParams: Map<String, String>, onSuccess: (PaymentDataResponse) -> Unit, onError: () -> Unit) {

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

    companion object {

        private val TAG = MainViewModel::class.java.simpleName
    }
}