package com.app.currency.util

import android.widget.TextView
import androidx.databinding.BindingAdapter

/**
 * Data binding functions.
 */
object BindingAdapters {

    @JvmStatic
    @BindingAdapter("dateString")
    fun displayDate(textView: TextView, dateString: String) {
        textView.text = getFormattedDate(
            dateString,
            inFormat = Constants.API_DATE_FORMAT,
            outFormat = Constants.DISPLAY_DATE_FORMAT
        )
    }

    @JvmStatic
    @BindingAdapter("rate")
    fun displayRate(textView: TextView, rate: Double) {
        textView.text = getFormattedAmount(rate)
    }

}