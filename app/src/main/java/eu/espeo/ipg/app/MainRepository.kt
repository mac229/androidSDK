package eu.espeo.ipg.app

import eu.espeo.ipg.api.Communication
import kotlinx.coroutines.*

/**
 * Created by Maciej Kozłowski on 2019-05-10.
 */
class MainRepository {

    private val apiService = Communication.apiService
    private var job: Job? = null

    fun fetchToken(customerId: String, merchantId: Long, onSuccess: (String) -> Unit, onError: () -> Unit) {
        job = CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = withContext(Dispatchers.IO) { apiService.getToken(customerId, merchantId).await() }
                onSuccess(result)
            } catch (exception: Exception) {
                onError()
            }
        }
    }

    fun cancelJob() {
        job?.cancel()
    }
}