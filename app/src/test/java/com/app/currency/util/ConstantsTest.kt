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

}