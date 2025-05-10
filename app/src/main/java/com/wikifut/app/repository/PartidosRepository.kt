package com.wikifut.app.repository


import com.wikifut.app.api.PartidosApi
import com.wikifut.app.model.ListaPartidos
import javax.inject.Inject

class PartidosRepository @Inject constructor(
    private val partidosApi: PartidosApi
){
    suspend fun getPartidos(fecha: String) : ListaPartidos {
        return partidosApi.getPartidos(fecha)
    }
    suspend fun getPartidosPorLigaYTemporada(leagueId: Int, season: Int): ListaPartidos {
        return partidosApi.getPartidosPorLigaYTemporada(leagueId, season)
    }

}