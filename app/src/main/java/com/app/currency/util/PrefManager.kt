package com.app.currency.util

import android.content.Context
import androidx.core.content.edit
import com.google.gson.JsonObject

class PrefManager(context: Context) {

    companion object {
        // Saves symbols json as string.
        const val SYMBOLS = "symbols"
    }

    private val pref = context.getSharedPreferences("CurrencyPref", Context.MODE_PRIVATE)

    fun getSymbols() = pref.getString(SYMBOLS, JsonObject().toString() /* Empty json object */)

    fun setSymbols(symbols: String?) {
        pref.edit(commit = true) { putString(SYMBOLS, symbols) }
    }

}