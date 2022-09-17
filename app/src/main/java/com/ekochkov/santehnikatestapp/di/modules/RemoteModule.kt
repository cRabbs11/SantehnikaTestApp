package com.ekochkov.santehnikatestapp.di.modules

import com.ekochkov.santehnikatestapp.BuildConfig
import com.ekochkov.santehnikatestapp.utils.API_KEYS
import com.ekochkov.santehnikatestapp.utils.Constants_API.PARAMETER_NAME_API_KEY
import com.ekochkov.santehnikatestapp.utils.Constants_API.YANDEX_GEOCODER_BASE_URL
import com.ekochkov.santehnikatestapp.utils.YandexRetrofitInterface
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class RemoteModule {

    private val CALL_TIMEOUT_MILLI_30 = 30L

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            callTimeout(CALL_TIMEOUT_MILLI_30, TimeUnit.SECONDS)
            readTimeout(CALL_TIMEOUT_MILLI_30, TimeUnit.SECONDS)

            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                }).build()
            }
            addInterceptor { chain ->
                val original = chain.request()
                val originalHttpUrl = original.url()
                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter(PARAMETER_NAME_API_KEY, API_KEYS.YANDEX_GEOCODER_API_KEY)
                    .build()
                val request = original.newBuilder()
                    .url(url)
                    .build()
                chain.proceed(request)
            }.build()
        }.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): YandexRetrofitInterface{
        return Retrofit.Builder()
            .baseUrl(YANDEX_GEOCODER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build().create(YandexRetrofitInterface::class.java)
    }
}