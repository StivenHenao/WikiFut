package com.wikifut.app.repository

import com.wikifut.app.api.PartidosApi
import com.wikifut.app.model.ListaEquipos
import com.wikifut.app.model.ListaLigas
import com.wikifut.app.model.ListaPlayers
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val partidosApi: PartidosApi
){
    suspend fun buscarEquipo(query: String) : ListaEquipos {
        return partidosApi.buscarEquipos(query)
    }
    suspend fun buscarLiga(query: String) : ListaLigas {
        return partidosApi.buscarLigas(query)
    }
    suspend fun buscarPlayer(query: String) : ListaPlayers {
        return partidosApi.buscarPlayers(query)
    }
}