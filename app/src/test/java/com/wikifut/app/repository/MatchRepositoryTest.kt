package com.wikifut.app.repository

// Importamos la interfaz de la API
import com.wikifut.app.api.PartidosApi
// Importamos los modelos que usaremos
import com.wikifut.app.model.*
// Importamos runBlocking para ejecutar funciones suspendidas en pruebas
import kotlinx.coroutines.runBlocking
// Importamos funciones de prueba de JUnit 
//prueba
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
// Importamos funciones de Mockito Kotlin para simular comportamientos
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

// Clase de prueba unitaria para MatchRepository
class MatchRepositoryTest {

    // Creamos un mock de PartidosApi, para simular su comportamiento
    private val partidosApi: PartidosApi = mock()

    // Repositorio a testear
    private lateinit var matchRepository: MatchRepository

    @Before
    fun setUp() {
        // Inicializamos el repositorio con el mock antes de cada prueba
        matchRepository = MatchRepository(partidosApi)
    }

    @Test
    fun `cuando se obtiene un partido por ID exitosamente, retorna el MatchResponse correcto`() = runBlocking {
        // ---------- Arrange ----------

        // ID de partido simulado
        val matchId = 12345L

        // Creamos el objeto MatchResponse esperado
        val expectedMatch = MatchResponse(
            fixture = MRFixture(
                id = matchId,
                referee = "John Doe",
                timezone = "UTC",
                date = "2024-06-14T19:00:00Z",
                timestamp = 1718397600L,
                periods = MRPeriods(first = 1, second = 2), // Tiempos del partido
                venue = MRVenue(
                    id = 1,
                    name = "Estadio Prueba",
                    city = "Ciudad X"
                ),
                status = MRStatus(
                    long = "Finished",
                    short = "FT",
                    elapsed = 90
                )
            ),
            league = MRLeague(
                id = 1,
                name = "Test League",
                country = "Country",
                logo = "",
                flag = "",
                season = 2024,
                round = "Round 1",
                standings = false
            ),
            teams = MRTeams(
                home = MRTeam(id = 1, name = "Equipo A", logo = ""),
                away = MRTeam(id = 2, name = "Equipo B", logo = "")
            ),
            goals = MRGoals(home = 2, away = 1),
            score = MRScore(
                halftime = MRGoals(1, 0),
                fulltime = MRGoals(2, 1),
                extratime = MRGoals(null, null),
                penalty = MRGoals(null, null)
            ),
            events = emptyList(),      // Sin eventos
            lineups = emptyList(),     // Sin alineaciones
            statistics = emptyList(),  // Sin estadísticas
            players = emptyList()      // Sin información de jugadores
        )

        // Indicamos que cuando se llame a getInfoPartido(matchId), se devuelva una lista con expectedMatch
        whenever(partidosApi.getInfoPartido(matchId)).thenReturn(
            MatchApiResponse(response = listOf(expectedMatch))
        )

        // ---------- Act ----------

        // Ejecutamos la función a probar
        val result = matchRepository.getMatchById(matchId)

        // ---------- Assert ----------

        // Verificamos que el resultado sea igual al esperado
        assertEquals(expectedMatch, result)
    }
}
