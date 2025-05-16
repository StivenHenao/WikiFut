package com.wikifut.app.api

import com.wikifut.app.model.ListaPartidos
import com.wikifut.app.model.ListaEquipos
import com.wikifut.app.model.ListaLigas
import com.wikifut.app.model.ListaPlayers
import retrofit2.http.GET
import retrofit2.http.Query

interface PartidosApi {
    @GET("fixtures")
    suspend fun getPartidos(@Query("date") fecha: String): ListaPartidos

    @GET("teams")
    suspend fun buscarEquipos(@Query("search") query: String): ListaEquipos

    @GET("leagues")
    suspend fun buscarLigas(@Query("search") query: String): ListaLigas

    @GET("players/profiles")
    suspend fun buscarPlayers(@Query("search") query: String): ListaPlayers
}
