package eu.espeo.ipg.api

import java.math.BigDecimal

data class SessionTokenRequest(
    val merchantId: Long,
    val password: String,
    val allowOriginUrl: String,
    val action: Action,
    val timestamp: Long,
    val channel: Channel,
    val amount: BigDecimal,
    val currency: Currency,
    val country: Country,
    val customerId: String? = null
)