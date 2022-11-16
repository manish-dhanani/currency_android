package com.app.currency.data.local

data class Symbol(
    val code: String,
    val currency: String
) {

    override fun toString() = code

}