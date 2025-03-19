package com.wikifut.app.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun convertirHoraAColombia(timestamp: Long): String {
    val date = Date(timestamp * 1000)
    val formato = SimpleDateFormat("hh:mm a", Locale.getDefault())
    formato.timeZone = TimeZone.getTimeZone("America/Bogota")
    return formato.format(date)
}