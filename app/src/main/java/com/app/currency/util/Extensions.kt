package com.app.currency.util

import android.content.Context
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.app.currency.R
import com.google.android.material.snackbar.Snackbar

object Extensions {

    /**
     * Snackbar with error message.
     */
    fun Context.showError(rootView: View, message: String) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).apply {
            view.setBackgroundColor(
                ContextCompat.getColor(this@showError, android.R.color.holo_red_dark)
            )

            (view.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView).apply {
                setTextColor(ContextCompat.getColor(this@showError, R.color.white))
                maxLines = 3
            }
        }.show()
    }

    /**
     * Toast message.
     */
    fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }

}