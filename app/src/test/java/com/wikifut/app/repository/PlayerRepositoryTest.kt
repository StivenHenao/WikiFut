package com.wikifut.app.repository

import com.wikifut.app.api.PlayerApi
import com.wikifut.app.model.PlayerDataResponse
import com.wikifut.app.model.Parameters
import com.wikifut.app.model.Paging
import com.wikifut.app.model.ResponseItem
import com.wikifut.app.model.Player
import com.wikifut.app.model.PlayerBirth
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import java.io.IOException
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class PlayerRepositoryTest {
    
    private val playerApi: PlayerApi = mock()
    private lateinit var playerRepository: PlayerRepository
    
    @Before
    fun setup() {
        playerRepository = PlayerRepository(playerApi)
    }
    
    @Test
    fun `cuando se obtiene un jugador exitosamente, retorna los datos correctos`() {
        runBlocking {
            // Arrange
            val playerId = 276
            val season = 2023
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
            
            whenever(playerApi.getPlayer(player = playerId, season = season)).thenReturn(expectedResponse)
            
            // Act
            val result = playerRepository.getPlayer(playerId, season)
            
            // Assert
            assertEquals(expectedResponse, result)
        }
    }

    @Test
    fun `cuando hay un error en la API, lanza una excepción`() {
        runBlocking {
            // Arrange
            val playerId = 276
            val season = 2023
            
            whenever(playerApi.getPlayer(player = playerId, season = season))
                .thenThrow(RuntimeException("Error de conexión"))
            
            // Act & Assert
            assertThrows(RuntimeException::class.java) {
                runBlocking {
                    playerRepository.getPlayer(playerId, season)
                }
            }
        }
    }

    @Test
    fun `cuando no se encuentra el jugador, retorna lista vacía`() {
        runBlocking {
            // Arrange
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
            
            whenever(playerApi.getPlayer(player = playerId, season = season)).thenReturn(emptyResponse)
            
            // Act
            val result = playerRepository.getPlayer(playerId, season)
            
            // Assert
            assertEquals(emptyResponse, result)
            assertEquals(0, result.results)
            assert(result.response.isEmpty())
        }
    }
} 