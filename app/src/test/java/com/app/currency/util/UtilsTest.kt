package com.app.currency.util

import org.junit.Assert
import org.junit.Test

internal class UtilsTest {

    @Test
    fun `validate formatted amount`() {
        Assert.assertEquals("1", getFormattedAmount(1.0))
        Assert.assertEquals("40.04", getFormattedAmount(40.04))
        Assert.assertEquals("100.789", getFormattedAmount(00100.789))
        Assert.assertEquals("87877575.5566", getFormattedAmount(87877575.5566))
        Assert.assertEquals("0.23232", getFormattedAmount(0.23232))
        Assert.assertEquals("123456.11667", getFormattedAmount(123456.11666777))
    }

}