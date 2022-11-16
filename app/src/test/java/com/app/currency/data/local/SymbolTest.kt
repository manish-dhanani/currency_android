package com.app.currency.data.local

import org.junit.Assert
import org.junit.Test

internal class SymbolTest {

    @Test
    fun `toString should return code`() {
        val symbol = Symbol("USD", "United States Dollar")
        Assert.assertEquals("USD", symbol.toString())
    }

}