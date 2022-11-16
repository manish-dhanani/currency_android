package com.app.currency.util

import java.text.DecimalFormat
import java.text.NumberFormat

fun getFormattedAmount(amount: Double): String {
    val numberFormat: NumberFormat = DecimalFormat(Constants.NUMBER_FORMAT)
    return numberFormat.format(amount)
}