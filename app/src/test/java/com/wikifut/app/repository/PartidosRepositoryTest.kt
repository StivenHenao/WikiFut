// Indica que esta clase está dentro del paquete com.wikifut.app.repository
package com.wikifut.app.repository

// Importa la interfaz que simula la API de partidos
import com.wikifut.app.api.PartidosApi
// Importa todos los modelos usados en los tests
import com.wikifut.app.model.*
// Permite ejecutar funciones suspendidas dentro de pruebas normales
import kotlinx.coroutines.runBlocking
// Función para hacer aserciones en los tests
import org.junit.Assert.assertEquals
// Anotación que indica que este método se ejecuta antes de cada test
import org.junit.Before
// Anotación que marca métodos como pruebas
import org.junit.Test
// Importa métodos para crear mocks
import org.mockito.kotlin.mock
// Importa función para definir comportamiento de mocks
import org.mockito.kotlin.whenever

// Clase de prueba para probar PartidosRepository
class PartidosRepositoryTest {

    // Crea un mock de la API de partidos
    private val partidosApi: PartidosApi = mock()

    // Declaración del repositorio que se va a testear
    private lateinit var partidosRepository: PartidosRepository

    // Inicializa el repositorio antes de cada prueba
    @Before
    fun setup() {
        partidosRepository = PartidosRepository(partidosApi)
    }

    // ---------------------- PRUEBA: getPartidos(fecha) ----------------------
    @Test
    fun `cuando se obtienen partidos por fecha exitosamente, retorna los datos correctos`() {
        // Ejecuta el código suspendido dentro de un contexto bloqueante
        runBlocking {
            // ------- Arrange: Prepara datos y comportamiento esperado -------
            val fecha = "2024-06-14" // Fecha para buscar partidos

            // Datos simulados que la API debería devolver
            val expectedResponse = ListaPartidos(
                response = listOf(
                    Partido(
                        fixture = Fixture(
                            id = 101,
                            timezone = "UTC",
                            date = "2024-06-14T19:00:00Z",
                            timestamp = 1718397600L,
                            venue = Venue(
                                id = 55,
                                name = "Estadio Ejemplo",
                                address = "Calle Falsa 123",
                                city = "Ciudad Fútbol",
                                capacity = 50000,
                                surface = "Grass",
                                image = "https://example.com/stadium.png"
                            ),
                            status = Status(
                                long = "Match Finished",
                                short = "FT",
                                elapsed = 90
                            )
                        ),
                        league = League(
                            id = 1,
                            name = "Champions League",
                            country = "Europe",
                            logo = "https://example.com/league_logo.png",
                            flag = "https://example.com/flag.png",
                            season = 2024,
                            round = "Group Stage - Round 1"
                        ),
                        teams = Teams(
                            home = Team(
                                id = 100,
                                name = "Equipo A",
                                logo = "https://example.com/teamA.png",
                                winner = true,
                                country = "España"
                            ),
                            away = Team(
                                id = 200,
                                name = "Equipo B",
                                logo = "https://example.com/teamB.png",
                                winner = false,
                                country = "Francia"
                            )
                        ),
                        goals = Goals(
                            home = 2,
                            away = 1
                        ),
                        score = Score(
                            halftime = ScoreDetail(home = 1, away = 0),
                            fulltime = ScoreDetail(home = 2, away = 1),
                            extratime = null,
                            penalty = null
                        )
                    )
                )
            )

            // Simula que el mock de la API retorna la respuesta esperada
            whenever(partidosApi.getPartidos(fecha)).thenReturn(expectedResponse)

            // ------- Act: Ejecuta el método real que se va a probar -------
            val result = partidosRepository.getPartidos(fecha)

            // ------- Assert: Compara el resultado real con el esperado -------
            assertEquals(expectedResponse, result)
        }
    }

    // ---------------------- PRUEBA: getPartidosPorLigaYTemporada() ----------------------
    @Test
    fun `cuando se obtienen partidos por liga y temporada exitosamente, retorna los datos correctos`() {
        runBlocking {
            // ------- Arrange -------
            val leagueId = 39
            val season = 2023

            val expectedResponse = ListaPartidos(
                response = listOf(
                    Partido(
                        fixture = Fixture(
                            id = 1234,
                            timezone = "UTC",
                            date = "2023-09-15T20:00:00Z",
                            timestamp = 1694808000L,
                            venue = Venue(
                                id = 999,
                                name = "Old Trafford",
                                address = "Sir Matt Busby Way",
                                city = "Manchester",
                                capacity = 74879,
                                surface = "Grass",
                                image = "https://example.com/old_trafford.png"
                            ),
                            status = Status(
                                long = "Match Finished",
                                short = "FT",
                                elapsed = 90
                            )
                        ),
                        league = League(
                            id = 39,
                            name = "Premier League",
                            country = "England",
                            logo = "https://example.com/epl_logo.png",
                            flag = "https://example.com/england_flag.png",
                            season = 2023,
                            round = "Week 5"
                        ),
                        teams = Teams(
                            home = Team(
                                id = 1,
                                name = "Manchester United",
                                logo = "https://example.com/mu_logo.png",
                                winner = true,
                                country = "England"
                            ),
                            away = Team(
                                id = 2,
                                name = "Arsenal",
                                logo = "https://example.com/arsenal_logo.png",
                                winner = false,
                                country = "England"
                            )
                        ),
                        goals = Goals(
                            home = 3,
                            away = 1
                        ),
                        score = Score(
                            halftime = ScoreDetail(home = 2, away = 1),
                            fulltime = ScoreDetail(home = 3, away = 1),
                            extratime = null,
                            penalty = null
                        )
                    )
                )
            )

            // Define el comportamiento simulado de la API para esta prueba
            whenever(partidosApi.getPartidosPorLigaYTemporada(leagueId, season)).thenReturn(expectedResponse)

            // ------- Act -------
            val result = partidosRepository.getPartidosPorLigaYTemporada(leagueId, season)

            // ------- Assert -------
            assertEquals(expectedResponse, result)
        }
    }
}
