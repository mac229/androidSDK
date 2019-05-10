package eu.espeo.ipg.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * Created by Maciej Kozłowski on 2019-05-10.
 */

object Communication {

    private const val BASE_URL = "https://cashierui-responsivedev.test.myriadpayments.com/ajax/"

    val service: ApiService

    init {
        val retrofit = Retrofit
            .Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(getHttpClient())
            .baseUrl(BASE_URL)
            .build()

        service = retrofit.create(ApiService::class.java)
    }

    private fun getHttpClient() = OkHttpClient.Builder().addInterceptor(getLoggingInterceptor()).build()

    private fun getLoggingInterceptor() = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
}