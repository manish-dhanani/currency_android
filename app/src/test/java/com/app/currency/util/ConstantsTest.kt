package com.app.currency.util

import org.junit.Assert
import org.junit.Test

internal class ConstantsTest {

    @Test
    fun `confirm fixer base url`() {
        Assert.assertEquals("https://api.apilayer.com/fixer/", Constants.FIXER_BASE_URL)
    }

    @Test
    fun `confirm number format`() {
        Assert.assertEquals("##.#####", Constants.NUMBER_FORMAT)
    }

    @Test
    fun `confirm date formats`() {
        Assert.assertEquals("yyyy-MM-dd", Constants.API_DATE_FORMAT)
        Assert.assertEquals("MMM dd, yyyy", Constants.DISPLAY_DATE_FORMAT)
    }

    @Test
    fun `confirm popular currencies with order`() {
        Assert.assertArrayEquals(
            arrayOf(
                "USD", "EUR", "GBP", "INR", "AUD", "CAD", "JPY", "CHF", "CNY", "ZAR", "NZD", "SGD"
            ), Constants.popularCurrencies
        )
    }

}