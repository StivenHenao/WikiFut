package com.wikifut.app.api

import com.wikifut.app.model.ListaPartidos
import retrofit2.http.GET
import retrofit2.http.Query

interface PartidosApi {
    @GET("fixtures")
    suspend fun getPartidos(@Query("date") fecha: String): ListaPartidos
}
