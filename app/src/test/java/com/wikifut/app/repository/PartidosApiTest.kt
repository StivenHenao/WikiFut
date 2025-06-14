package com.wikifut.app.api

import com.wikifut.app.model.Fixture
import com.wikifut.app.model.ListaPartidos
import com.wikifut.app.model.Partido
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PartidosApiTest {

    private val api = mockk<PartidosApi>()

    @Test
    fun `getPartidos devuelve lista simulada de partidos`() = runTest {
        val fixtureMock = Fixture(
            id = 1001,
            timezone = "UTC",
            date = "2025-06-14T17:00:00Z",
            timestamp = 1234567890L,
            venue = com.wikifut.app.model.Venue(
                id = 10,
                name = "Camp Nou",
                city = "Barcelona",
                address = "",
                capacity = 0,
                surface = "",
                image = ""
            ),
            status = com.wikifut.app.model.Status(
                long = "Not Started",
                short = "NS",
                elapsed = null
            )
        )

        val leagueMock = mockk<com.wikifut.app.model.League>()
        every { leagueMock.id } returns 100

        val fecha = "2025-06-14"
        val partidosMock = ListaPartidos(
            response = listOf(
                Partido(
                    fixture = fixtureMock,
                    league = leagueMock,
                    teams = mockk(),
                    goals = mockk(),
                    score = mockk()
                )
            )
        )

        coEvery { api.getPartidos(fecha) } returns partidosMock

        val resultado = api.getPartidos(fecha)

        println("Resultado de prueba: $resultado")
        assertEquals(1, resultado.response.size)
        assertEquals(100, resultado.response.first().league.id)
        assertEquals(1001, resultado.response.first().fixture.id)
    }

    @Test
    fun `getPartidos lanza excepcion en error de red`() = runTest {
        val fecha = "2025-06-15"
        coEvery { api.getPartidos(fecha) } throws RuntimeException("Error de red")

        try {
            api.getPartidos(fecha)
            assert(false) { "Se esperaba una excepción" }
        } catch (e: RuntimeException) {
            assertEquals("Error de red", e.message)
        }
    }
}
