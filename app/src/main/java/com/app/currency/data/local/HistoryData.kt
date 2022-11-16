package com.app.currency.data.local

data class HistoryData(
    val base: String,
    val target: String,
    val date: String,
    val rate: Double
)
