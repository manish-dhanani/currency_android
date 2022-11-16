package com.app.currency.data.local

class ConvertResult(
    val info: Info,
    val amount: Double
) {

    class Info(
        val timestamp: Long,
        val rate: Double
    )

}