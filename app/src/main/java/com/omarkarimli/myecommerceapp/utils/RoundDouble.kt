package com.omarkarimli.myecommerceapp.utils

import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

fun roundDouble(n: Double): Double {
    val locale = Locale.getDefault()
    val numberFormat = NumberFormat.getInstance(locale)
    return numberFormat.parse(n.toBigDecimal().setScale(1, RoundingMode.HALF_EVEN).toDouble().toString())?.toDouble() ?: 0.0
}
