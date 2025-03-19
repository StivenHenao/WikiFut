package com.wikifut.app.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


fun obtenerFechaActual(): String {
    return LocalDate.now().format(DateTimeFormatter.ISO_DATE) // Formato YYYY-MM-DD
}

fun formatFechaParaApi(date: Date): String {
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return outputFormat.format(date)
}