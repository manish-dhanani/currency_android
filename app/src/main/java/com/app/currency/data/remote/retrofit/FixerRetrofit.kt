package com.app.currency.data.remote.retrofit

import com.google.gson.JsonElement
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * The Interface defines Fixer APIs.
 */
interface FixerRetrofit {

    @GET("symbols")
    suspend fun getSymbols(): JsonElement

    @GET("convert")
    suspend fun convert(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: Double
    ): JsonElement

}