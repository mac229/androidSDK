package eu.espeo.ipg.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * Created by Maciej Kozłowski on 2019-05-10.
 */

object Communication {

    private const val BASE_URL = "https://cashierui-responsivedev.test.myriadpayments.com/ajax/"

    val apiService: ApiService

    init {
        val retrofit = Retrofit
            .Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(getHttpClient())
            .baseUrl(BASE_URL)
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    private fun getHttpClient() = OkHttpClient.Builder().addInterceptor(getLoggingInterceptor()).build()

    private fun getLoggingInterceptor() = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
}