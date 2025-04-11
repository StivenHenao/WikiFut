package com.wikifut.app.api

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
}
