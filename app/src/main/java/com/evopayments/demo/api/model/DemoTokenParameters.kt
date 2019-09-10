package com.evopayments.demo.api.model

class DemoTokenParameters(
    merchantId: String = "167885",
    password: String = "56789",
    customerId: String = "lovelyrita",
    currency: String = "PLN",
    country: String = "PL",
    amount: String = "2.00",
    action: String = "AUTH",
    allowOriginUrl: String = "http://example.com",
    language: String = "pl",
    myriadFlowId: String = ""
) : HashMap<String, String>() {
    init {
        put("merchantId", merchantId)
        put("password", password)
        put("customerId", customerId)
        put("currency", currency)
        put("country", country)
        put("amount", amount)
        put("action", action)
        put("allowOriginUrl", allowOriginUrl)
        put("language", language)
        put("myriadFlowId", myriadFlowId)
    }
}
