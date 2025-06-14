// Paquete donde se ubica esta clase de pruebas
package com.wikifut.app.repository

// Importación de la interfaz que usaremos como mock
import com.wikifut.app.api.PlayerApi

// Importación de clases del modelo que simulan la respuesta esperada
import com.wikifut.app.model.PlayerDataResponse
import com.wikifut.app.model.Parameters
import com.wikifut.app.model.Paging
import com.wikifut.app.model.ResponseItem
import com.wikifut.app.model.Player
import com.wikifut.app.model.PlayerBirth

// Permite ejecutar corrutinas en tests sin necesidad de ser suspend functions
import kotlinx.coroutines.runBlocking

// Herramientas de JUnit para comprobar resultados y excepciones
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows

// Excepción que podríamos simular para errores de red
import java.io.IOException

// Mockito Kotlin para crear mocks y simular comportamientos
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

// Declaración de la clase de pruebas
class PlayerRepositoryTest {

    // Creamos un mock de la API de jugadores
    private val playerApi: PlayerApi = mock()

    // Instancia que se va a probar
    private lateinit var playerRepository: PlayerRepository

    // Antes de cada test, inicializamos el repositorio con la API mockeada
    @Before
    fun setup() {
        playerRepository = PlayerRepository(playerApi)
    }

    // Test para verificar que al obtener un jugador exitosamente, los datos devueltos son correctos
    @Test
    fun `cuando se obtiene un jugador exitosamente, retorna los datos correctos`() {
        runBlocking {
            // Arrange: definimos datos de entrada esperados
            val playerId = 276
            val season = 2023

            // Creamos una respuesta mock que imita lo que devolvería la API
            val expectedResponse = PlayerDataResponse(
                get = "players",
                parameters = Parameters(
                    id = playerId,
                    team = null,
                    league = null,
                    season = season,
                    search = null,
                    player = null
                ),
                errors = null,
                results = 1,
                paging = Paging(current = 1, total = 1),
                response = listOf(
                    ResponseItem(
                        player = Player(
                            id = playerId,
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
                            photo = "https://example.com/messi.jpg"
                        ),
                        statistics = null
                    )
                )
            )

            // when: configuramos el comportamiento del mock
            whenever(playerApi.getPlayer(player = playerId, season = season)).thenReturn(expectedResponse)

            // Act: llamamos a la función del repositorio
            val result = playerRepository.getPlayer(playerId, season)

            // Assert: verificamos que el resultado sea igual al esperado
            assertEquals(expectedResponse, result)
        }
    }

    // Test para simular un error de red o fallo inesperado
    @Test
    fun `cuando hay un error en la API, lanza una excepción`() {
        runBlocking {
            // Arrange: definimos los datos
            val playerId = 276
            val season = 2023

            // Simulamos que la llamada lanza una excepción
            whenever(playerApi.getPlayer(player = playerId, season = season))
                .thenThrow(RuntimeException("Error de conexión"))

            // Act & Assert: se espera que se lance una excepción al llamar al repositorio
            assertThrows(RuntimeException::class.java) {
                runBlocking {
                    playerRepository.getPlayer(playerId, season)
                }
            }
        }
    }

    // Test para caso donde no se encuentra el jugador (respuesta vacía)
    @Test
    fun `cuando no se encuentra el jugador, retorna lista vacía`() {
        runBlocking {
            // Arrange: configuramos una respuesta vacía
            val playerId = 999999
            val season = 2023

            val emptyResponse = PlayerDataResponse(
                get = "players",
                parameters = Parameters(
                    id = playerId,
                    team = null,
                    league = null,
                    season = season,
                    search = null,
                    player = null
                ),
                errors = null,
                results = 0,
                paging = Paging(current = 1, total = 0),
                response = emptyList()
            )

            // Simulamos que la API devuelve esta respuesta vacía
            whenever(playerApi.getPlayer(player = playerId, season = season)).thenReturn(emptyResponse)

            // Act: llamamos al repositorio
            val result = playerRepository.getPlayer(playerId, season)

            // Assert: verificamos que la respuesta sea la esperada y esté vacía
            assertEquals(emptyResponse, result)
            assertEquals(0, result.results)
            assert(result.response.isEmpty())
        }
    }
}
