package com.app.currency.util

class Constants {

    companion object {
        const val FIXER_BASE_URL = "https://api.apilayer.com/fixer/"
        const val NUMBER_FORMAT = "##.#####"
        const val API_DATE_FORMAT = "yyyy-MM-dd"
        const val DISPLAY_DATE_FORMAT = "MMM dd, yyyy"

        val popularCurrencies = arrayOf(
            "USD", "EUR", "GBP", "INR", "AUD", "CAD", "JPY", "CHF", "CNY", "ZAR", "NZD", "SGD"
        )
    }

}