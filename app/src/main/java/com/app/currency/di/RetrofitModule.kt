package com.app.currency.di

import android.content.Context
import com.app.currency.R
import com.app.currency.data.remote.retrofit.FixerRetrofit
import com.app.currency.util.Constants.Companion.FIXER_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * The Dagger Module provides instance built on [Retrofit.Builder] to make network calls.
 */
@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideRetrofit(retrofit: Retrofit.Builder): FixerRetrofit =
        retrofit.build().create(FixerRetrofit::class.java)

    @Singleton
    @Provides
    fun provideRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit.Builder = Retrofit.Builder()
        .baseUrl(FIXER_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())

    @Singleton
    @Provides
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient =
        OkHttpClient().newBuilder()
            .apply {
                // Add network interceptor to OkHttpClient, to set apikey as Header.
                addInterceptor { chain ->
                    val newRequestBuilder = chain.request().newBuilder()
                    newRequestBuilder.addHeader("apikey", context.getString(R.string.fixer_apikey))
                    chain.proceed(newRequestBuilder.build())
                }

                // Set timeout to OkHttpClient.
                connectTimeout(90, TimeUnit.SECONDS)
                readTimeout(90, TimeUnit.SECONDS)
                writeTimeout(90, TimeUnit.SECONDS)

            }.build()

}