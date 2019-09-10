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
        put(MERCHANT_ID, merchantId)
        put(PASSWORD, password)
        put(CUSTOMER_ID, customerId)
        put(CURRENCY, currency)
        put(COUNTRY, country)
        put(AMOUNT, amount)
        put(ACTION, action)
        put(ALLOW_ORIGIN_URL, allowOriginUrl)
        put(LANGUAGE, language)
        put(MY_RIAD_FLOW_ID, myriadFlowId)
    }

    fun getMerchantId() = get(MERCHANT_ID)

    fun getPassword() = get(PASSWORD)

    fun getCustomerId() = get(CUSTOMER_ID)

    fun getCurrency() = get(CURRENCY)

    fun getCountry() = get(COUNTRY)

    fun getAmount() = get(AMOUNT)

    fun getAction() = get(ACTION)

    fun getAllowOriginUrl() = get(ALLOW_ORIGIN_URL)

    fun getLanguage() = get(LANGUAGE)

    fun getMyRiadFlowId() = get(MY_RIAD_FLOW_ID)

    companion object {
        private const val MERCHANT_ID = "merchantId"
        private const val PASSWORD = "password"
        private const val CUSTOMER_ID = "customerId"
        private const val CURRENCY = "currency"
        private const val COUNTRY = "country"
        private const val AMOUNT = "amount"
        private const val ACTION = "action"
        private const val ALLOW_ORIGIN_URL = "allowOriginUrl"
        private const val LANGUAGE = "language"
        private const val MY_RIAD_FLOW_ID = "myriadFlowId"
    }
}
