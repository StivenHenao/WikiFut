package com.wikifut.app.api

import com.wikifut.app.model.PlayersByLeagueResponse
import com.wikifut.app.model.TeamsResponse
import com.wikifut.app.model.LigaDetalleResponse
import com.wikifut.app.model.StandingsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LigaDetalleApi {

    @GET("leagues")
    suspend fun getLigaDetalle(
        @Query("id") leagueId: Int
    ): Response<LigaDetalleResponse>

    @GET("standings")
    suspend fun getTablaPosiciones(
        @Query("league") leagueId: Int,
        @Query("season") season: Int
    ): Response<StandingsResponse>

    @GET("teams")
    suspend fun getTeamsByLeagueAndSeason(
        @Query("league") leagueId: Int,
        @Query("season") season: Int
    ): Response<TeamsResponse>

    @GET("players")
    suspend fun getPlayersByLeagueAndSeason(
        @Query("league") leagueId: Int,
        @Query("season") season: Int,
        @Query("page") page: Int = 1
    ): Response<PlayersByLeagueResponse>
}
