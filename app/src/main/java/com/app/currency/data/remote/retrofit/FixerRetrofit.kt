package com.app.currency.data.remote.retrofit

import com.google.gson.JsonElement
import retrofit2.http.GET

/**
 * The Interface defines Fixer APIs.
 */
interface FixerRetrofit {

    @GET("symbols")
    suspend fun getSymbols(): JsonElement

}