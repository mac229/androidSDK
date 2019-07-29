package com.evopayments.demo.app

import com.evopayments.demo.api.Communication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * Created by Maciej Kozłowski on 2019-05-10.
 */
class MainRepository(override val coroutineContext: CoroutineContext) : CoroutineScope {

    private val apiService = Communication.apiService

    fun fetchToken(customerId: String, merchantId: Long, onSuccess: (String) -> Unit, onError: () -> Unit) {
        launch {
            try {
                val result = apiService.getToken(customerId, merchantId)
                onSuccess(result)
            } catch (exception: Exception) {
                onError()
            }
        }
    }
}