package com.app.currency.util

import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

fun getFormattedAmount(amount: Double): String {
    val numberFormat: NumberFormat = DecimalFormat(Constants.NUMBER_FORMAT)
    return numberFormat.format(amount)
}

/**
 * Returns date in outFormat.
 */
fun getFormattedDate(dateString: String, inFormat: String, outFormat: String): String {
    SimpleDateFormat(inFormat, Locale.ENGLISH).parse(dateString)?.let {
        return SimpleDateFormat(outFormat, Locale.ENGLISH).format(it)
    }

    // Return input date if unable to parse.
    return dateString
}