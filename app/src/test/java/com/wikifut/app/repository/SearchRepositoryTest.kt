// Paquete del archivo de pruebas
package com.wikifut.app.repository

// Importación de la API que se va a simular (mockear)
import com.wikifut.app.api.PartidosApi

// Importaciones de modelos usados en las respuestas simuladas
import com.wikifut.app.model.CountryInfo
import com.wikifut.app.model.Coverage
import com.wikifut.app.model.EquipoInfo
import com.wikifut.app.model.League
import com.wikifut.app.model.LigaResponse
import com.wikifut.app.model.ListaEquipos
import com.wikifut.app.model.ListaLigas
import com.wikifut.app.model.ListaPlayers
import com.wikifut.app.model.Player
import com.wikifut.app.model.PlayerBirth
import com.wikifut.app.model.PlayerInfo
import com.wikifut.app.model.Season
import com.wikifut.app.model.Team
import com.wikifut.app.model.Venue

// Corrutinas para pruebas con funciones suspendidas
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

// Funciones y anotaciones para pruebas unitarias
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows

// Librería Mockito Kotlin para crear mocks
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

// Clase que contiene los tests de SearchRepository
class SearchRepositoryTest {

    // Mock de la API para simular respuestas sin acceder a la red
    private val partidosApi: PartidosApi = mock()

    // Repositorio a probar
    private lateinit var searchRepository: SearchRepository

    // Se ejecuta antes de cada test, inicializa el repositorio con el mock
    @Before
    fun setup() {
        searchRepository = SearchRepository(partidosApi)
    }

    // ---------------------- TEST buscarEquipo() ----------------------

    /*
     * Test para verificar que buscarEquipo() devuelve correctamente
     * los datos simulados de un equipo (como FC Barcelona)
     */
    @Test
    fun `cuando se busca un equipo exitosamente, retorna los datos correctos`() {
        runBlocking {
            // Arrange: Datos esperados
            val query = "Barcelona"
            val expectedResponse = ListaEquipos(
                response = listOf(
                    EquipoInfo(
                        team = Team(
                            id = 1,
                            name = "FC Barcelona",
                            logo = "https://example.com/barcelona_logo.png",
                            winner = null,
                            country = "Spain"
                        ),
                        venue = Venue(
                            id = 10,
                            name = "Spotify Camp Nou",
                            address = "C. d'Arístides Maillol",
                            city = "Barcelona",
                            capacity = 99354,
                            surface = "Grass",
                            image = "https://example.com/campnou.png"
                        )
                    )
                )
            )

            // Se configura el mock para devolver la respuesta esperada
            whenever(partidosApi.buscarEquipos(query)).thenReturn(expectedResponse)

            // Act: Se llama a la función del repositorio
            val result = searchRepository.buscarEquipo(query)

            // Assert: Se comprueba que el resultado sea el esperado
            assertEquals(expectedResponse, result)
            assertEquals("FC Barcelona", result.response.first().team.name)
            assertEquals("Spotify Camp Nou", result.response.first().venue.name)
        }
    }

    // ---------------------- TEST buscarLiga() ----------------------

    /*
     * Test para verificar que buscarLiga() devuelve correctamente
     * los datos de una liga como "La Liga" de España
     */
    @Test
    fun `cuando se busca una liga exitosamente, retorna los datos correctos`() {
        runBlocking {
            // Arrange: Se prepara una respuesta simulada sin coverage
            val query = "La Liga"
            val expectedResponse = ListaLigas(
                response = listOf(
                    LigaResponse(
                        league = League(
                            id = 140,
                            name = "La Liga",
                            country = "Spain",
                            logo = "https://example.com/laliga_logo.png",
                            flag = "https://example.com/spain_flag.png",
                            season = 2023,
                            round = "Regular Season - 10"
                        ),
                        country = CountryInfo(
                            name = "Spain",
                            flag = "https://example.com/spain_flag.png"
                        ),
                        seasons = emptyList() // Aquí no se usa Coverage
                    )
                )
            )

            // Se configura el mock para devolver esa respuesta
            whenever(partidosApi.buscarLigas(query)).thenReturn(expectedResponse)

            // Act: Se ejecuta la función del repositorio
            val result = searchRepository.buscarLiga(query)

            // Assert: Se comprueba que los datos devueltos sean los correctos
            assertEquals(expectedResponse, result)
            assertEquals("La Liga", result.response.first().league.name)
            assertEquals("Spain", result.response.first().country.name)
        }
    }

    // ---------------------- TEST buscarPlayer() ----------------------

    /*
     * Test para verificar que buscarPlayer() devuelve correctamente
     * los datos simulados de un jugador (como Messi)
     */
    @Test
    fun `cuando se busca un jugador exitosamente, retorna los datos correctos`() {
        runBlocking {
            // Arrange: Datos esperados del jugador
            val query = "Messi"
            val expectedResponse = ListaPlayers(
                response = listOf(
                    PlayerInfo(
                        player = Player(
                            id = 10,
                            name = "Lionel Messi",
                            firstname = "Lionel",
                            lastname = "Messi",
                            age = 36,
                            birth = PlayerBirth(
                                date = "1987-06-24",
                                place = "Rosario",
                                country = "Argentina"
                            ),
                            nationality = "Argentina",
                            height = "170 cm",
                            weight = "72 kg",
                            number = 10,
                            position = "Forward",
                            photo = "https://example.com/messi.jpg",
                            injured = false
                        )
                    )
                )
            )

            // Se configura el mock para devolver esa respuesta
            whenever(partidosApi.buscarPlayers(query)).thenReturn(expectedResponse)

            // Act: Se llama al método de búsqueda
            val result = searchRepository.buscarPlayer(query)

            // Assert: Se valida el resultado
            assertEquals(expectedResponse, result)
            assertEquals("Lionel Messi", result.response.first().player.name)
        }
    }
}
