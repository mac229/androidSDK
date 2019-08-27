package com.evopayments.demo.api

import com.evopayments.demo.api.model.PaymentDataResponse
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by Maciej Kozłowski on 2019-05-10.
 */
interface ApiService {

    @POST("tokenJson")
    @FormUrlEncoded
    suspend fun getToken(
        @FieldMap tokenParams: Map<String, String>
    ): PaymentDataResponse
}