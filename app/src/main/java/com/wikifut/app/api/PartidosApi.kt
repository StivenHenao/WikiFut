package com.wikifut.app.api

import com.wikifut.app.model.ListaPartidos
import com.wikifut.app.model.MatchApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PartidosApi {
    @GET("fixtures")
    suspend fun getPartidos(@Query("date") fecha: String): ListaPartidos

    // https://v3.football.api-sports.io/fixtures?id=354
    @GET("fixtures")
    suspend fun getInfoPartido(@Query("id") id: Long): MatchApiResponse
}
