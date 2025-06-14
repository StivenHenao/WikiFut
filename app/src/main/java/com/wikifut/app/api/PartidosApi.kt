package com.wikifut.app.api

import com.wikifut.app.model.ListaPartidos
import com.wikifut.app.model.MatchApiResponse
import com.wikifut.app.model.ListaEquipos
import com.wikifut.app.model.ListaLigas
import com.wikifut.app.model.ListaPlayers
import com.wikifut.app.model.TeamStatisticsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PartidosApi {
    @GET("fixtures")
    suspend fun getPartidos(@Query("date") fecha: String): ListaPartidos

    // https://v3.football.api-sports.io/fixtures?id=354
    @GET("fixtures")
    suspend fun getInfoPartido(@Query("id") id: Long): MatchApiResponse

    @GET("teams")
    suspend fun buscarEquipos(@Query("search") query: String): ListaEquipos

    @GET("fixtures")
    suspend fun getPartidosPorLigaYTemporada(
        @Query("league") leagueId: Int,
        @Query("season") season: Int
    ): ListaPartidos


    @GET("teams/statistics")
    suspend fun getTeamStats(
        @Query("league") leagueId: Int,
        @Query("season") season: Int,
        @Query("team") teamId: Int
    ): TeamStatisticsResponse


    @GET("leagues")
    suspend fun buscarLigas(@Query("search") query: String): ListaLigas

    @GET("leagues")
    suspend fun obtenerLigasPorEquipoYTemporada(
        @Query("team") teamId: Int,
        @Query("season") season: Int
    ): ListaLigas

    @GET("players/profiles")
    suspend fun buscarPlayers(@Query("search") query: String): ListaPlayers
}
