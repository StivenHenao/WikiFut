package com.wikifut.app.repository

import com.wikifut.app.api.PartidosApi
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever


class SearchRepositoryTest {

    private val partidosApi: PartidosApi = mock()

    private lateinit var searchRepository: SearchRepository

    @Before
    fun setup() {
        searchRepository = SearchRepository(partidosApi)
    }


    /*
    Test de la función buscarEquipo
    */
    @Test
    fun `cuando se busca un equipo exitosamente, retorna los datos correctos`() {
        runBlocking {
            // Arrange
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

            // Simulación del comportamiento del mock
            whenever(partidosApi.buscarEquipos(query)).thenReturn(expectedResponse)

            // Act
            val result = searchRepository.buscarEquipo(query)

            // Assert
            assertEquals(expectedResponse, result)
            assertEquals("FC Barcelona", result.response.first().team.name)
            assertEquals("Spotify Camp Nou", result.response.first().venue.name)
        }
    }



    /*
  Test de la función buscarLiga
  */
    @Test
    fun `cuando se busca una liga exitosamente, retorna los datos correctos`() {
        runBlocking {
            // Arrange
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
                        seasons = emptyList() // se elimina la parte con Coverage
                    )
                )
            )

            whenever(partidosApi.buscarLigas(query)).thenReturn(expectedResponse)

            // Act
            val result = searchRepository.buscarLiga(query)

            // Assert
            assertEquals(expectedResponse, result)
            assertEquals("La Liga", result.response.first().league.name)
            assertEquals("Spain", result.response.first().country.name)
        }
    }



    /*
    Test de la función buscarPlayer
    */
    @Test
    fun `cuando se busca un jugador exitosamente, retorna los datos correctos`() {
        runBlocking {
            // Arrange
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

            // Simulamos que la API devuelve esta respuesta
            whenever(partidosApi.buscarPlayers(query)).thenReturn(expectedResponse)

            // Act
            val result = searchRepository.buscarPlayer(query)

            // Assert
            assertEquals(expectedResponse, result)
            assertEquals("Lionel Messi", result.response.first().player.name)
        }
    }

}
