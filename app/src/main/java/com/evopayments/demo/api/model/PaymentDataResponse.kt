package com.evopayments.demo.api.model

import com.squareup.moshi.Json

data class PaymentDataResponse(

    @Json(name = "cashierUrl")
    val cashierUrl: String,

    @Json(name = "token")
    val token: String
)
