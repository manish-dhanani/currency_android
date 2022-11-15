package com.app.currency.util

import org.junit.Assert
import org.junit.Test

class ConstantsTest {

    @Test
    fun `confirm fixer base url`() {
        Assert.assertEquals("https://api.apilayer.com/fixer/", Constants.FIXER_BASE_URL)
    }

}