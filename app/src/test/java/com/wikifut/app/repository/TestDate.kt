package com.wikifut.app.repository

import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class TestDate {

    // Función que simula tu función en producción
    private fun formatFechaParaApi(date: Date): String {
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return outputFormat.format(date)
    }

    @Test
    fun `formatFechaParaApi formatea correctamente la fecha de hoy`() {
        // Dada: una fecha conocida – 1 de enero de 2025
        val calendar = Calendar.getInstance().apply {
            set(2025, Calendar.JANUARY, 1, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val date = calendar.time

        // Cuando: formateas esta fecha
        val resultado = formatFechaParaApi(date)

        // Entonces: debe ser “2025-01-01”
        assertEquals("2025-01-01", resultado)
    }

    @Test
    fun `formatFechaParaApi formatea correctamente fecha actual`() {
        val ahora = Date()
        val esperado = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(ahora)

        val resultadoAhora = formatFechaParaApi(ahora)
        assertEquals(esperado, resultadoAhora)
    }

}
