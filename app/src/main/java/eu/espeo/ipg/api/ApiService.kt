package eu.espeo.ipg.api

import kotlinx.coroutines.Deferred
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by Maciej Kozłowski on 2019-05-10.
 */
interface ApiService {

    @POST("token")
    fun getToken(@Query("customerId") customerId: String, @Query("merchantId") merchantId: Long): Deferred<String>
}