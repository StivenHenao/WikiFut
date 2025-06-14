// Paquete donde está ubicado este archivo de prueba
package com.wikifut.app.api

// Se importan las clases necesarias para simular datos del modelo
import com.wikifut.app.model.Fixture
import com.wikifut.app.model.ListaPartidos
import com.wikifut.app.model.Partido

// Librerías de mockeo (para simular comportamientos sin usar datos reales)
import io.mockk.coEvery // Permite simular funciones suspendidas
import io.mockk.every    // Permite simular propiedades o funciones normales
import io.mockk.mockk    // Crea objetos simulados (mock)

// Librerías para testear corrutinas
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

// Librerías de JUnit para hacer aserciones (comparaciones)
import org.junit.Assert.assertEquals
import org.junit.Test

// Anotación para permitir usar funciones de corrutinas en pruebas
@OptIn(ExperimentalCoroutinesApi::class)
class PartidosApiTest {

    // Se crea un objeto simulado del API que se va a testear
    private val api = mockk<PartidosApi>()

    // Primer test: verificar que la función devuelve correctamente una lista simulada de partidos
    @Test
    fun `getPartidos devuelve lista simulada de partidos`() = runTest {
        // Se crea un objeto Fixture simulado con datos ficticios
        val fixtureMock = Fixture(
            id = 1001,
            timezone = "UTC",
            date = "2025-06-14T17:00:00Z",
            timestamp = 1234567890L,
            venue = com.wikifut.app.model.Venue( // Venue también simulado con datos ficticios
                id = 10,
                name = "Camp Nou",
                city = "Barcelona",
                address = "",
                capacity = 0,
                surface = "",
                image = ""
            ),
            status = com.wikifut.app.model.Status( // Estado del partido
                long = "Not Started",
                short = "NS",
                elapsed = null
            )
        )

        // Se crea un mock del objeto League con una propiedad id simulada
        val leagueMock = mockk<com.wikifut.app.model.League>()
        every { leagueMock.id } returns 100 // Se define que cuando se acceda a "leagueMock.id", retorne 100

        // Fecha que se usará para simular la petición
        val fecha = "2025-06-14"

        // Se crea un objeto ListaPartidos que contiene un único partido simulado
        val partidosMock = ListaPartidos(
            response = listOf(
                Partido(
                    fixture = fixtureMock,
                    league = leagueMock,
                    teams = mockk(), // Los equipos, goles y score son simulados pero no relevantes aquí
                    goals = mockk(),
                    score = mockk()
                )
            )
        )

        // Se define el comportamiento del metodo getPartidos del API simulado: cuando se llame con esa fecha, devolverá el mock
        coEvery { api.getPartidos(fecha) } returns partidosMock

        // Se llama a la función simulada
        val resultado = api.getPartidos(fecha)

        // Se imprime el resultado en consola para verlo durante las pruebas
        println("Resultado de prueba: $resultado")

        // Se verifica que el tamaño de la lista sea 1
        assertEquals(1, resultado.response.size)
        // Se verifica que el ID de la liga del primer partido sea 100
        assertEquals(100, resultado.response.first().league.id)
        // Se verifica que el ID del fixture sea 1001
        assertEquals(1001, resultado.response.first().fixture.id)
    }

    // Segundo test: se simula un error al obtener los partidos
    @Test
    fun `getPartidos lanza excepcion en error de red`() = runTest {
        val fecha = "2025-06-15"

        // Se define que si se llama a getPartidos con esa fecha, se lanzará una excepción
        coEvery { api.getPartidos(fecha) } throws RuntimeException("Error de red")

        try {
            // Se intenta llamar a la función, esperando que falle
            api.getPartidos(fecha)
            // Si no lanza excepción, el test falla
            assert(false) { "Se esperaba una excepción" }
        } catch (e: RuntimeException) {
            // Se verifica que el mensaje de la excepción sea el esperado
            assertEquals("Error de red", e.message)
        }
    }
}
