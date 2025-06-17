package com.wikifut.app.repository

import com.wikifut.app.api.PartidosApi
import com.wikifut.app.model.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class PartidosTest {

    private val partidosApi: PartidosApi = mock()

    private lateinit var partidosRepository: PartidosRepository

    @Before
    fun setup() {
        partidosRepository = PartidosRepository(partidosApi)
    }

    @Test
    fun `cuando se obtienen partidos por fecha exitosamente, retorna los datos correctos`() {
        runBlocking {
            // Arrange
            val fecha = "2024-06-14"
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

            whenever(partidosApi.getPartidos(fecha)).thenReturn(expectedResponse)

            // Act
            val result = partidosRepository.getPartidos(fecha)

            // Assert
            assertEquals(expectedResponse, result)
        }
    }

    @Test
    fun `cuando se obtienen partidos por liga y temporada exitosamente, retorna los datos correctos`() {
        runBlocking {
            // Arrange
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

            whenever(partidosApi.getPartidosPorLigaYTemporada(leagueId, season)).thenReturn(expectedResponse)

            // Act
            val result = partidosRepository.getPartidosPorLigaYTemporada(leagueId, season)

            // Assert
            assertEquals(expectedResponse, result)
        }
    }
}
