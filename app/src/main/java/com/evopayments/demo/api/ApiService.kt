package com.evopayments.demo.api

import com.evopayments.demo.api.model.PaymentDataResponse
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by Maciej Kozłowski on 2019-05-10.
 */
interface ApiService {

    @POST("tokenJson")
    suspend fun getToken(@Query("customerId") customerId: String, @Query("merchantId") merchantId: Long): PaymentDataResponse
}