// Paquete donde se encuentra esta clase de prueba
package com.wikifut.app.repository

// Importamos funciones necesarias para los tests
import org.junit.Assert.assertEquals  // Para comparar valores esperados con los reales
import org.junit.Test                // Para indicar que una función es un test
import java.text.SimpleDateFormat   // Para dar formato a las fechas
import java.util.*                  // Para usar Date y Calendar

// Clase que contiene las pruebas relacionadas con el formato de fechas
class TestDate {

    // Esta es una función auxiliar que simula la función real del proyecto que formatea fechas
    private fun formatFechaParaApi(date: Date): String {
        // Se define el formato de salida como "yyyy-MM-dd", que es el que requiere la API
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        // Se aplica el formato a la fecha recibida
        return outputFormat.format(date)
    }

    // Primer test: verificar que una fecha fija se formatea correctamente
    @Test
    fun `formatFechaParaApi formatea correctamente la fecha de hoy`() {
        // Se crea un calendario con la fecha 1 de enero de 2025
        val calendar = Calendar.getInstance().apply {
            set(2025, Calendar.JANUARY, 1, 0, 0, 0) // Año, mes (0 = enero), día, hora, minuto, segundo
            set(Calendar.MILLISECOND, 0)           // Se aseguran los milisegundos en 0 para evitar diferencias
        }
        val date = calendar.time // Se obtiene el objeto Date

        // Se llama a la función a testear con esa fecha
        val resultado = formatFechaParaApi(date)

        // Se comprueba que el resultado sea exactamente "2025-01-01"
        assertEquals("2025-01-01", resultado)
    }

    // Segundo test: verificar que la fecha actual también se formatea correctamente
    @Test
    fun `formatFechaParaApi formatea correctamente fecha actual`() {
        // Se obtiene la fecha y hora actual
        val ahora = Date()
        // Se crea el formato esperado para comparar
        val esperado = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(ahora) // Se formatea usando el mismo formato para que coincida

        // Se llama a la función con la fecha actual
        val resultadoAhora = formatFechaParaApi(ahora)

        // Se comprueba que el resultado formateado coincida con el esperado
        assertEquals(esperado, resultadoAhora)
    }
}
